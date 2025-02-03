package org.universiapolis.fablab.pfe.schedulingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.universiapolis.fablab.pfe.schedulingservice.model.Appointment;
import org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByServiceType(String serviceType);
    // If you need to check for overlapping appointments based on serviceType and startTime:
    boolean existsByServiceTypeAndStartTimeBetween(
            String serviceType,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
    @Modifying
    @Query("UPDATE Appointment a SET a.status = org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus.CANCELLED WHERE a.patientId = :patientId")
    void cancelAppointmentsByPatientId(Long patientId);
}