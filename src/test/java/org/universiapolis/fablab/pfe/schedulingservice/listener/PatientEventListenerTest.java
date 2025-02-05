package org.universiapolis.fablab.pfe.schedulingservice.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.universiapolis.fablab.pfe.schedulingservice.repository.AppointmentRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PatientEventListenerTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PatientEventListener patientEventListener;

    @Test
    public void testHandlePatientDeletedEvent() {
        // Act
        patientEventListener.handlePatientDeletedEvent(123L);

        // Assert
        verify(appointmentRepository).cancelAppointmentsByPatientId(123L);
    }
}