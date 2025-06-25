package customer_management_service.service;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import customer_management_service.dto.CustomerStatsDTO;
import customer_management_service.exception.CustomerNotFoundException;
import customer_management_service.exception.InvalidDataException;
import customer_management_service.mapper.CustomerMapper;
import customer_management_service.model.Customer;
import customer_management_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for customer business logic operations.
 * Handles CRUD operations, validations, and business rules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private static final int RETIREMENT_AGE = 65;

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerMessagingService customerMessagingService;

    /**
     * Creates a new customer.
     * Automatically calculates estimated retirement date and sends asynchronous message.
     * 
     * @param customerDTO customer data to create
     * @return the created customer
     */
    @Transactional
    public CustomerDTO createCustomer(CustomerCreateDTO customerDTO) {
        validateAgeMatchesBirthDate(customerDTO.getAge(), customerDTO.getBirthDate());
        
        Customer customer = customerMapper.toEntity(customerDTO);
        
        customer.setEstimatedEventDate(getEstimatedEventDate(customer));
        
        Customer savedCustomer = customerRepository.save(customer);
        
        // Send asynchronous event notification
        customerMessagingService.sendCustomerCreatedEvent(savedCustomer);
        
        return customerMapper.toDTO(savedCustomer);
    }

    /**
     * Gets all customers ordered by creation date descending.
     * 
     * @return list of all customers
     */
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAllByOrderByCreationDateDesc().stream()
            .map(customerMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Gets a customer by ID.
     * 
     * @param id customer ID
     * @return the found customer
     * @throws CustomerNotFoundException if customer is not found
     */
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toDTO(customer);
    }

    /**
     * Gets general statistics of all customers.
     * 
     * @return statistics with average age, standard deviation and total customers
     */
    public CustomerStatsDTO getCustomerStats() {
        Double averageAge = customerRepository.getAverageAge();
        Double standardDeviation = customerRepository.getAgeStandardDeviation();
        Long totalCustomers = customerRepository.count();
        
        return new CustomerStatsDTO(averageAge, standardDeviation, totalCustomers);
    }

    /**
     * Gets the average age of all customers.
     * 
     * @return average age
     */
    public Double getAverageAge() {
        return customerRepository.getAverageAge();
    }

    /**
     * Gets the standard deviation of customer ages.
     * 
     * @return standard deviation of ages
     */
    public Double getAgeStandardDeviation() {
        return customerRepository.getAgeStandardDeviation();
    }

    /**
     * Updates an existing customer.
     * Only updates fields provided in the DTO.
     * Recalculates retirement date if birth date changes.
     * 
     * @param id customer ID to update
     * @param customerDTO update data
     * @return the updated customer
     * @throws CustomerNotFoundException if customer is not found
     */
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerUpdateDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));

        validateUpdateData(customerDTO);
        customerMapper.updateEntityFromDTO(customer, customerDTO);
        updateEstimatedEventDateIfNeeded(customer, customerDTO);

        Customer updatedCustomer = customerRepository.save(customer);
        
        // Send asynchronous event notification
        customerMessagingService.sendCustomerUpdatedEvent(updatedCustomer);
        
        return customerMapper.toDTO(updatedCustomer);
    }

    /**
     * Validates the update data if both age and birth date are provided.
     * 
     * @param customerDTO the update data to validate
     */
    private void validateUpdateData(CustomerUpdateDTO customerDTO) {
        if (customerDTO.getAge() != null && customerDTO.getBirthDate() != null) {
            validateAgeMatchesBirthDate(customerDTO.getAge(), customerDTO.getBirthDate());
        }
    }

    /**
     * Updates the estimated event date if birth date was changed.
     * 
     * @param customer the customer to update
     * @param customerDTO the update data
     */
    private void updateEstimatedEventDateIfNeeded(Customer customer, CustomerUpdateDTO customerDTO) {
        if (customerDTO.getBirthDate() != null) {
            customer.setEstimatedEventDate(getEstimatedEventDate(customer));
        }
    }

    /**
     * Deletes a customer by ID.
     * Sends asynchronous message with deleted customer ID.
     * 
     * @param id customer ID to delete
     * @throws CustomerNotFoundException if customer is not found
     */
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
            
        customerRepository.delete(customer);
        
        // Send asynchronous event notification
        customerMessagingService.sendCustomerDeletedEvent(id);
    }

    /**
     * Valida que la edad proporcionada coincida con la fecha de nacimiento
     * 
     * @param age edad proporcionada
     * @param birthDate fecha de nacimiento
     */
    private void validateAgeMatchesBirthDate(Integer age, LocalDate birthDate) {
        if (age == null || birthDate == null) {
            return;
        }
        
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Period period = Period.between(birthDate, today);
        int calculatedAge = period.getYears();
        
        if (Math.abs(age - calculatedAge) > 1) {
            throw new InvalidDataException(
                String.format("Age %d does not match birth date %s (calculated age: %d)", 
                    age, birthDate, calculatedAge)
            );
        }
    }

    /**
     * Calculates the estimated event date (retirement) based on customer's birth date.
     * 
     * @param customer the customer to calculate the event date for
     * @return the estimated event date
     */
    private LocalDate getEstimatedEventDate(Customer customer) {
        return customer.getBirthDate().plusYears(RETIREMENT_AGE);
    }
} 