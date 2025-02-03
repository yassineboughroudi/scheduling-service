package org.universiapolis.fablab.pfe.schedulingservice.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.universiapolis.fablab.pfe.schedulingservice.repository.AppointmentRepository;

@Component
@RequiredArgsConstructor
public class PatientEventListener {
    private final AppointmentRepository appointmentRepository;

    @RabbitListener(queues = "scheduling.patient.deleted")
    @Transactional
    public void handlePatientDeletedEvent(Long patientId) {
        appointmentRepository.cancelAppointmentsByPatientId(patientId);
    }
}

