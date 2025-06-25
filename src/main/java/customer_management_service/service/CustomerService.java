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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private static final int RETIREMENT_AGE = 65;
    private static final String CUSTOMER_EVENTS_EXCHANGE = "customer.events";

    private final CustomerRepository customerRepository;
    private final RabbitTemplate rabbitTemplate;
    private final CustomerMapper customerMapper;

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
        
        LocalDate estimatedEventDate = customer.getBirthDate().plusYears(RETIREMENT_AGE);
        customer.setEstimatedEventDate(estimatedEventDate);
        
        Customer savedCustomer = customerRepository.save(customer);
        
        sendMessageSafely(CUSTOMER_EVENTS_EXCHANGE, "customer.created", savedCustomer);
        
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

        if (customerDTO.getAge() != null && customerDTO.getBirthDate() != null) {
            validateAgeMatchesBirthDate(customerDTO.getAge(), customerDTO.getBirthDate());
        }

        customerMapper.updateEntityFromDTO(customer, customerDTO);
        
        if (customerDTO.getBirthDate() != null) {
            LocalDate estimatedEventDate = customer.getBirthDate().plusYears(RETIREMENT_AGE);
            customer.setEstimatedEventDate(estimatedEventDate);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        
        sendMessageSafely(CUSTOMER_EVENTS_EXCHANGE, "customer.updated", updatedCustomer);
        
        return customerMapper.toDTO(updatedCustomer);
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
        
        sendMessageSafely(CUSTOMER_EVENTS_EXCHANGE, "customer.deleted", id);
    }

    /**
     * Sends a message safely, handling RabbitMQ errors.
     * 
     * @param exchange destination exchange
     * @param routingKey routing key
     * @param message message to send
     */
    private void sendMessageSafely(String exchange, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("Message sent successfully to {} with routing key {}", exchange, routingKey);
        } catch (Exception e) {
            log.warn("Failed to send message to RabbitMQ: {}", e.getMessage());
        }
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
        
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        int calculatedAge = period.getYears();
        
        if (Math.abs(age - calculatedAge) > 1) {
            throw new InvalidDataException(
                String.format("Age %d does not match birth date %s (calculated age: %d)", 
                    age, birthDate, calculatedAge)
            );
        }
    }
} 