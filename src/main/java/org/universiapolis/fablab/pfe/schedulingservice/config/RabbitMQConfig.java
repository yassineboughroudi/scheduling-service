package org.universiapolis.fablab.pfe.schedulingservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange for patient events (e.g., patient deleted)
    @Bean
    public TopicExchange patientExchange() {
        return new TopicExchange("patient.events");
    }

    // Queue for patient.deleted events
    @Bean
    public Queue patientDeletedQueue() {
        return new Queue("scheduling.patient.deleted");
    }

    // Bind patient.deleted queue to the exchange
    @Bean
    public Binding patientDeletedBinding() {
        return BindingBuilder.bind(patientDeletedQueue())
                .to(patientExchange())
                .with("patient.deleted");
    }
}