package org.universiapolis.fablab.pfe.schedulingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.universiapolis.fablab.pfe.schedulingservice.dto.request.AppointmentRequest;
import org.universiapolis.fablab.pfe.schedulingservice.dto.response.AppointmentResponse;
import org.universiapolis.fablab.pfe.schedulingservice.exception.AppointmentConflictException;
import org.universiapolis.fablab.pfe.schedulingservice.exception.PatientNotFoundException;
import org.universiapolis.fablab.pfe.schedulingservice.model.Appointment;
import org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus;
import org.universiapolis.fablab.pfe.schedulingservice.repository.AppointmentRepository;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate;

    public AppointmentResponse createAppointment(AppointmentRequest request) {
        validatePatient(request.getPatientId());

        // Check for overlapping appointments
        if (appointmentRepository.existsByServiceTypeAndStartTimeBetween(
                request.getServiceType(),
                request.getStartTime(),
                request.getEndTime()
        )) {
            throw new AppointmentConflictException("Time slot unavailable");
        }

        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .serviceType(request.getServiceType())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToAppointmentResponse(savedAppointment);
    }

    private void validatePatient(Long patientId) {
        String patientServiceUrl = "http://patient-management-service:8082/api/patients/" + patientId;
        try {
            restTemplate.getForEntity(patientServiceUrl, Void.class);
        } catch (Exception e) {
            throw new PatientNotFoundException("Patient not found: " + patientId);
        }
    }

    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .serviceType(appointment.getServiceType())
                .status(appointment.getStatus())
                .build();
    }
}