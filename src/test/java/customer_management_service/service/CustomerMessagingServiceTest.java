package customer_management_service.service;

import customer_management_service.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("CustomerMessagingService Tests")
class CustomerMessagingServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CustomerMessagingService customerMessagingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should send customer created event successfully")
    void shouldSendCustomerCreatedEventSuccessfully() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAge(30);
        customer.setBirthDate(LocalDate.of(1994, 1, 1));

        // When
        customerMessagingService.sendCustomerCreatedEvent(customer);

        // Then
        verify(rabbitTemplate).convertAndSend(
            eq("customer.events"), 
            eq("customer.created"), 
            eq(customer)
        );
    }

    @Test
    @DisplayName("Should send customer updated event successfully")
    void shouldSendCustomerUpdatedEventSuccessfully() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setAge(31);
        customer.setBirthDate(LocalDate.of(1993, 1, 1));

        // When
        customerMessagingService.sendCustomerUpdatedEvent(customer);

        // Then
        verify(rabbitTemplate).convertAndSend(
            eq("customer.events"), 
            eq("customer.updated"), 
            eq(customer)
        );
    }

    @Test
    @DisplayName("Should send customer deleted event successfully")
    void shouldSendCustomerDeletedEventSuccessfully() {
        // Given
        Long customerId = 1L;

        // When
        customerMessagingService.sendCustomerDeletedEvent(customerId);

        // Then
        verify(rabbitTemplate).convertAndSend(
            eq("customer.events"), 
            eq("customer.deleted"), 
            eq(customerId)
        );
    }

    @Test
    @DisplayName("Should handle RabbitMQ exception gracefully")
    void shouldHandleRabbitMQExceptionGracefully() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        
        doThrow(new RuntimeException("RabbitMQ connection failed"))
            .when(rabbitTemplate).convertAndSend(eq("customer.events"), eq("customer.created"), any(Object.class));

        // When & Then
        // Should not throw exception
        customerMessagingService.sendCustomerCreatedEvent(customer);
        
        // Verify the method was still called
        verify(rabbitTemplate).convertAndSend(
            eq("customer.events"), 
            eq("customer.created"), 
            eq(customer)
        );
    }

    @Test
    @DisplayName("Should handle multiple exceptions gracefully")
    void shouldHandleMultipleExceptionsGracefully() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        
        doThrow(new RuntimeException("Connection failed"))
            .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        // When & Then
        // All methods should handle exceptions gracefully
        customerMessagingService.sendCustomerCreatedEvent(customer);
        customerMessagingService.sendCustomerUpdatedEvent(customer);
        customerMessagingService.sendCustomerDeletedEvent(1L);
        
        // Verify all methods were called
        verify(rabbitTemplate, times(3)).convertAndSend(anyString(), anyString(), any(Object.class));
    }
} 