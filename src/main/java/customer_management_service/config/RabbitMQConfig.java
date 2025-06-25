package customer_management_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CUSTOMER_EVENTS_EXCHANGE = "customer.events";
    public static final String CUSTOMER_CREATED_QUEUE = "customer.created.queue";
    public static final String CUSTOMER_UPDATED_QUEUE = "customer.updated.queue";
    public static final String CUSTOMER_DELETED_QUEUE = "customer.deleted.queue";

    @Bean
    public TopicExchange customerEventsExchange() {
        return new TopicExchange(CUSTOMER_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue customerCreatedQueue() {
        return new Queue(CUSTOMER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue customerUpdatedQueue() {
        return new Queue(CUSTOMER_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue customerDeletedQueue() {
        return new Queue(CUSTOMER_DELETED_QUEUE, true);
    }

    @Bean
    public Binding customerCreatedBinding() {
        return BindingBuilder.bind(customerCreatedQueue())
                .to(customerEventsExchange())
                .with("customer.created");
    }

    @Bean
    public Binding customerUpdatedBinding() {
        return BindingBuilder.bind(customerUpdatedQueue())
                .to(customerEventsExchange())
                .with("customer.updated");
    }

    @Bean
    public Binding customerDeletedBinding() {
        return BindingBuilder.bind(customerDeletedQueue())
                .to(customerEventsExchange())
                .with("customer.deleted");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 