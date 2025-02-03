package org.universiapolis.fablab.pfe.schedulingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.universiapolis.fablab.pfe.schedulingservice.dto.request.AppointmentRequest;
import org.universiapolis.fablab.pfe.schedulingservice.dto.response.AppointmentResponse;
import org.universiapolis.fablab.pfe.schedulingservice.service.SchedulingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentRequest request
    ) {
        AppointmentResponse response = schedulingService.createAppointment(request);
        return ResponseEntity.ok(response);
    }
}