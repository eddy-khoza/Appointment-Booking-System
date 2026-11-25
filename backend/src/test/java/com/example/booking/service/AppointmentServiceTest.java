package com.example.booking.service;

import com.example.booking.dto.AppointmentRequest;
import com.example.booking.dto.AppointmentResponse;
import com.example.booking.entity.Appointment;
import com.example.booking.exception.ConflictException;
import com.example.booking.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock AppointmentRepository repository;
    @Mock EmailService emailService;

    @InjectMocks AppointmentService service;

    private AppointmentRequest request;

    @BeforeEach
    void setUp() {
        request = new AppointmentRequest();
        request.setCustomerName("Jane Doe");
        request.setCustomerEmail("jane@abs.com");
        request.setAppointmentTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void book_success() {
        when(repository.findByAppointmentTimeBetween(any(), any())).thenReturn(Collections.emptyList());
        when(repository.save(any(Appointment.class))).thenAnswer(inv -> {
            Appointment a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        AppointmentResponse response = service.book(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo("CONFIRMED");
        verify(emailService).sendConfirmation(any());
    }

    @Test
    void book_conflict_throws() {
        Appointment existing = new Appointment();
        existing.setStatus("CONFIRMED");
        when(repository.findByAppointmentTimeBetween(any(), any()))
                .thenReturn(List.of(existing));

        assertThatThrownBy(() -> service.book(request))
                .isInstanceOf(ConflictException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void book_emailFailure_doesNotRollback() {
        when(repository.findByAppointmentTimeBetween(any(), any())).thenReturn(Collections.emptyList());
        when(repository.save(any(Appointment.class))).thenAnswer(inv -> {
            Appointment a = inv.getArgument(0);
            a.setId(2L);
            return a;
        });
        doThrow(new RuntimeException("smtp down")).when(emailService).sendConfirmation(any());

        AppointmentResponse response = service.book(request);

        assertThat(response.getId()).isEqualTo(2L);
    }
}