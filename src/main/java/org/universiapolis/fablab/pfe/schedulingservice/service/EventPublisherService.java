package org.universiapolis.fablab.pfe.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publishAppointmentConfirmedEvent(Long appointmentId) {
        rabbitTemplate.convertAndSend(
                "appointment.events",
                "appointment.confirmed",
                "Appointment confirmed: " + appointmentId
        );
    }
}