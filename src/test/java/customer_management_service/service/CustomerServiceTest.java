package customer_management_service.service;

import customer_management_service.dto.CustomerCreateDTO;
import customer_management_service.dto.CustomerDTO;
import customer_management_service.dto.CustomerUpdateDTO;
import customer_management_service.dto.CustomerStatsDTO;
import customer_management_service.exception.InvalidDataException;
import customer_management_service.mapper.CustomerMapper;
import customer_management_service.model.Customer;
import customer_management_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_ShouldCalculateEstimatedEventDate() {
        // Arrange
        CustomerCreateDTO createDTO = new CustomerCreateDTO();
        createDTO.setFirstName("John");
        createDTO.setLastName("Doe");
        createDTO.setAge(30);
        createDTO.setBirthDate(LocalDate.of(1994, 1, 1));

        Customer customer = new Customer();
        customer.setFirstName(createDTO.getFirstName());
        customer.setLastName(createDTO.getLastName());
        customer.setAge(createDTO.getAge());
        customer.setBirthDate(createDTO.getBirthDate());

        CustomerDTO expectedDTO = new CustomerDTO();
        expectedDTO.setFirstName(customer.getFirstName());
        expectedDTO.setLastName(customer.getLastName());
        expectedDTO.setAge(customer.getAge());
        expectedDTO.setBirthDate(customer.getBirthDate());
        expectedDTO.setEstimatedEventDate(LocalDate.of(2059, 1, 1));

        when(customerMapper.toEntity(createDTO)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(expectedDTO);

        // Act
        CustomerDTO result = customerService.createCustomer(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2059, 1, 1), result.getEstimatedEventDate());
        verify(rabbitTemplate).convertAndSend(eq("customer.events"), eq("customer.created"), any(Customer.class));
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        // Arrange
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        List<Customer> customers = Arrays.asList(customer1, customer2);

        CustomerDTO dto1 = new CustomerDTO();
        CustomerDTO dto2 = new CustomerDTO();
        List<CustomerDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(customerRepository.findAllByOrderByCreationDateDesc()).thenReturn(customers);
        when(customerMapper.toDTO(customer1)).thenReturn(dto1);
        when(customerMapper.toDTO(customer2)).thenReturn(dto2);

        // Act
        List<CustomerDTO> result = customerService.getAllCustomers();

        // Assert
        assertEquals(expectedDTOs.size(), result.size());
        verify(customerRepository).findAllByOrderByCreationDateDesc();
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);

        CustomerDTO expectedDTO = new CustomerDTO();
        expectedDTO.setId(id);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(expectedDTO);

        // Act
        CustomerDTO result = customerService.getCustomerById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(customerRepository).findById(id);
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenCustomerNotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerStats_ShouldReturnAllStats() {
        // Arrange
        when(customerRepository.getAverageAge()).thenReturn(30.0);
        when(customerRepository.getAgeStandardDeviation()).thenReturn(5.0);
        when(customerRepository.count()).thenReturn(10L);

        // Act
        CustomerStatsDTO result = customerService.getCustomerStats();

        // Assert
        assertNotNull(result);
        assertEquals(30.0, result.getAverageAge());
        assertEquals(5.0, result.getAgeStandardDeviation());
        assertEquals(10L, result.getTotalCustomers());
        verify(customerRepository).getAverageAge();
        verify(customerRepository).getAgeStandardDeviation();
        verify(customerRepository).count();
    }

    @Test
    void updateCustomer_ShouldUpdateAndReturnCustomer() {
        // Arrange
        Long id = 1L;
        Customer existingCustomer = new Customer();
        existingCustomer.setId(id);
        existingCustomer.setFirstName("John");
        existingCustomer.setLastName("Doe");
        existingCustomer.setAge(30);
        existingCustomer.setBirthDate(LocalDate.of(1994, 1, 1));

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setLastName("Smith");
        updateDTO.setAge(31);
        updateDTO.setBirthDate(LocalDate.of(1993, 1, 1));

        CustomerDTO expectedDTO = new CustomerDTO();
        expectedDTO.setId(id);
        expectedDTO.setFirstName("Jane");
        expectedDTO.setLastName("Smith");
        expectedDTO.setAge(31);
        expectedDTO.setBirthDate(LocalDate.of(1993, 1, 1));
        expectedDTO.setEstimatedEventDate(LocalDate.of(2058, 1, 1));

        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);
        when(customerMapper.toDTO(existingCustomer)).thenReturn(expectedDTO);

        // Act
        CustomerDTO result = customerService.updateCustomer(id, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(31, result.getAge());
        assertEquals(LocalDate.of(2058, 1, 1), result.getEstimatedEventDate());
        verify(rabbitTemplate).convertAndSend(eq("customer.events"), eq("customer.updated"), any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(id);

        // Assert
        verify(customerRepository).delete(customer);
        verify(rabbitTemplate).convertAndSend(eq("customer.events"), eq("customer.deleted"), eq(id));
    }

    @Test
    void getAverageAge_ShouldReturnCorrectAverage() {
        // Arrange
        when(customerRepository.getAverageAge()).thenReturn(30.0);

        // Act
        Double result = customerService.getAverageAge();

        // Assert
        assertEquals(30.0, result);
        verify(customerRepository).getAverageAge();
    }

    @Test
    void getAgeStandardDeviation_ShouldReturnCorrectDeviation() {
        // Arrange
        when(customerRepository.getAgeStandardDeviation()).thenReturn(5.0);

        // Act
        Double result = customerService.getAgeStandardDeviation();

        // Assert
        assertEquals(5.0, result);
        verify(customerRepository).getAgeStandardDeviation();
    }
} 