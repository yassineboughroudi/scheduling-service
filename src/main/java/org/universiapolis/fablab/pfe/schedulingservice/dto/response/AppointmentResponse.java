package org.universiapolis.fablab.pfe.schedulingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String serviceType;
    private AppointmentStatus status;
}