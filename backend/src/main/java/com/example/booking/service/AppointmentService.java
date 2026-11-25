package com.example.booking.service;

import com.example.booking.dto.AppointmentRequest;
import com.example.booking.dto.AppointmentResponse;
import com.example.booking.entity.Appointment;
import com.example.booking.exception.ConflictException;
import com.example.booking.exception.NotFoundException;
import com.example.booking.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    private static final long SLOT_MINUTES = 30;

    private final AppointmentRepository repository;
    private final EmailService emailService;

    public AppointmentService(AppointmentRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Transactional
    public AppointmentResponse book(AppointmentRequest request) {
        LocalDateTime slotStart = request.getAppointmentTime().minusMinutes(SLOT_MINUTES / 2);
        LocalDateTime slotEnd = request.getAppointmentTime().plusMinutes(SLOT_MINUTES / 2);

        List<Appointment> conflicts = repository.findByAppointmentTimeBetween(slotStart, slotEnd);
        boolean hasActiveConflict = conflicts.stream()
                .anyMatch(a -> "CONFIRMED".equals(a.getStatus()));
        if (hasActiveConflict) {
            throw new ConflictException("Time slot already booked: " + request.getAppointmentTime());
        }

        Appointment apt = new Appointment();
        apt.setCustomerName(request.getCustomerName());
        apt.setCustomerEmail(request.getCustomerEmail());
        apt.setAppointmentTime(request.getAppointmentTime());
        apt.setNotes(request.getNotes());
        apt.setStatus("CONFIRMED");

        Appointment saved = repository.save(apt);

        try {
            emailService.sendConfirmation(saved);
        } catch (Exception e) {
            log.warn("Mocked email failed for appointment {}: {}", saved.getId(), e.getMessage());
        }

        log.info("Booked appointment id={} for {}", saved.getId(), saved.getCustomerEmail());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findByEmail(String email) {
        return repository.findByCustomerEmailOrderByAppointmentTimeDesc(email)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment apt = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + id));
        if ("CANCELLED".equals(apt.getStatus())) {
            throw new ConflictException("Appointment already cancelled: " + id);
        }
        apt.setStatus("CANCELLED");
        Appointment saved = repository.save(apt);
        try {
            emailService.sendCancellation(saved);
        } catch (Exception e) {
            log.warn("Simulated cancel email failed for appointment {}: {}", saved.getId(), e.getMessage());
        }
        return toResponse(saved);
    }

    private AppointmentResponse toResponse(Appointment apt) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(apt.getId());
        r.setCustomerName(apt.getCustomerName());
        r.setCustomerEmail(apt.getCustomerEmail());
        r.setAppointmentTime(apt.getAppointmentTime());
        r.setStatus(apt.getStatus());
        r.setNotes(apt.getNotes());
        r.setCreatedAt(apt.getCreatedAt());
        return r;
    }
}