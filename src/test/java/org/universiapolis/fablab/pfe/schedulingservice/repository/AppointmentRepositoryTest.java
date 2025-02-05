package org.universiapolis.fablab.pfe.schedulingservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.universiapolis.fablab.pfe.schedulingservice.model.Appointment;
import org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    public void testExistsByServiceTypeAndStartTimeBetween() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        Appointment appointment = Appointment.builder()
                .patientId(123L)
                .startTime(start)
                .endTime(end)
                .serviceType("MRI")
                .status(AppointmentStatus.PENDING)
                .build();
        appointmentRepository.save(appointment);

        // Act & Assert
        assertTrue(appointmentRepository.existsByServiceTypeAndStartTimeBetween(
                "MRI", start.minusMinutes(30), end.plusMinutes(30)
        ));
    }
}