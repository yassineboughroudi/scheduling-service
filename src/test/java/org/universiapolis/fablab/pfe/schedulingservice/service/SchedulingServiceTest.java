package org.universiapolis.fablab.pfe.schedulingservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.universiapolis.fablab.pfe.schedulingservice.dto.request.AppointmentRequest;
import org.universiapolis.fablab.pfe.schedulingservice.dto.response.AppointmentResponse;
import org.universiapolis.fablab.pfe.schedulingservice.exception.AppointmentConflictException;
import org.universiapolis.fablab.pfe.schedulingservice.exception.PatientNotFoundException;
import org.universiapolis.fablab.pfe.schedulingservice.model.Appointment;
import org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus;
import org.universiapolis.fablab.pfe.schedulingservice.repository.AppointmentRepository;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchedulingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SchedulingService schedulingService;

    @Test
    public void testCreateAppointment_Success() {
        // Arrange
        AppointmentRequest request = buildValidAppointmentRequest();
        Appointment savedAppointment = buildAppointmentFromRequest(request);

        when(appointmentRepository.existsByServiceTypeAndStartTimeBetween(
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class))
        ).thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        // Act
        AppointmentResponse response = schedulingService.createAppointment(request);

        // Assert
        assertNotNull(response);
        assertEquals(AppointmentStatus.PENDING, response.getStatus());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    public void testCreateAppointment_PatientNotFound() {
        // Arrange
        AppointmentRequest request = buildValidAppointmentRequest();
        String patientServiceUrl = "http://patient-management-service:8082/api/patients/123";

        when(restTemplate.getForEntity(patientServiceUrl, Void.class))
                .thenThrow(new RuntimeException("Patient not found"));

        // Act & Assert
        assertThrows(PatientNotFoundException.class, () -> schedulingService.createAppointment(request));
    }

    @Test
    public void testCreateAppointment_TimeConflict() {
        // Arrange
        AppointmentRequest request = buildValidAppointmentRequest();

        when(appointmentRepository.existsByServiceTypeAndStartTimeBetween(
                anyString(), any(LocalDateTime.class), any(LocalDateTime.class))
        ).thenReturn(true);

        // Act & Assert
        assertThrows(AppointmentConflictException.class, () -> schedulingService.createAppointment(request));
    }

    private AppointmentRequest buildValidAppointmentRequest() {
        return AppointmentRequest.builder()
                .patientId(123L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .serviceType("MRI")
                .build();
    }

    private Appointment buildAppointmentFromRequest(AppointmentRequest request) {
        return Appointment.builder()
                .id(1L)
                .patientId(request.getPatientId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .serviceType(request.getServiceType())
                .status(AppointmentStatus.PENDING)
                .build();
    }
}