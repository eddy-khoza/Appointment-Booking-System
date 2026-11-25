package com.example.booking.controller;

import com.example.booking.dto.AppointmentRequest;
import com.example.booking.dto.AppointmentResponse;
import com.example.booking.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentController.class)
class AppointmentControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockBean AppointmentService service;

    @Test
    void book_badRequest_whenInvalid() throws Exception {
        AppointmentRequest r = new AppointmentRequest();
        mvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(r)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void book_created_whenValid() throws Exception {
        AppointmentResponse stub = new AppointmentResponse();
        stub.setId(1L);
        stub.setStatus("CONFIRMED");
        when(service.book(any())).thenReturn(stub);

        AppointmentRequest r = new AppointmentRequest();
        r.setCustomerName("Jane Doe");
        r.setCustomerEmail("jane@example.com");
        r.setAppointmentTime(LocalDateTime.now().plusDays(2));

        mvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(r)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}