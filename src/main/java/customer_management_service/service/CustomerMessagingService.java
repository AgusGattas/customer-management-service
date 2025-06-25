package customer_management_service.service;

import customer_management_service.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling asynchronous messaging operations
 * related to customer events using RabbitMQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerMessagingService {

    private static final String CUSTOMER_EVENTS_EXCHANGE = "customer.events";

    private final RabbitTemplate rabbitTemplate;

    /**
     * Sends a customer created event message.
     * 
     * @param customer the created customer
     */
    public void sendCustomerCreatedEvent(Customer customer) {
        sendMessageSafely(CUSTOMER_EVENTS_EXCHANGE, "customer.created", customer);
    }

    /**
     * Sends a customer updated event message.
     * 
     * @param customer the updated customer
     */
    public void sendCustomerUpdatedEvent(Customer customer) {
        sendMessageSafely(CUSTOMER_EVENTS_EXCHANGE, "customer.updated", customer);
    }

    /**
     * Sends a customer deleted event message.
     * 
     * @param customerId the ID of the deleted customer
     */
    public void sendCustomerDeletedEvent(Long customerId) {
        sendMessageSafely(CUSTOMER_EVENTS_EXCHANGE, "customer.deleted", customerId);
    }

    /**
     * Sends a message safely, handling RabbitMQ errors gracefully.
     * This method ensures that messaging failures don't affect the main business logic.
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
            // Note: We don't throw the exception to avoid affecting the main business logic
            // In a production environment, you might want to implement retry logic or
            // store failed messages for later processing
        }
    }
} 