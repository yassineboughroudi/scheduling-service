package org.universiapolis.fablab.pfe.schedulingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.universiapolis.fablab.pfe.schedulingservice.dto.request.AppointmentRequest;
import org.universiapolis.fablab.pfe.schedulingservice.dto.response.AppointmentResponse;
import org.universiapolis.fablab.pfe.schedulingservice.model.AppointmentStatus;
import org.universiapolis.fablab.pfe.schedulingservice.service.SchedulingService;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SchedulingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchedulingService schedulingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAppointment_Success() throws Exception {
        // Arrange
        AppointmentRequest request = buildValidAppointmentRequest();
        AppointmentResponse response = buildAppointmentResponse(request);

        when(schedulingService.createAppointment(any(AppointmentRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void testCreateAppointment_InvalidRequest() throws Exception {
        // Arrange
        AppointmentRequest invalidRequest = AppointmentRequest.builder()
                .patientId(null) // Invalid: patientId is required
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    private AppointmentRequest buildValidAppointmentRequest() {
        return AppointmentRequest.builder()
                .patientId(123L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .serviceType("MRI")
                .build();
    }

    private AppointmentResponse buildAppointmentResponse(AppointmentRequest request) {
        return AppointmentResponse.builder()
                .id(1L)
                .patientId(request.getPatientId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .serviceType(request.getServiceType())
                .status(AppointmentStatus.PENDING)
                .build();
    }
}