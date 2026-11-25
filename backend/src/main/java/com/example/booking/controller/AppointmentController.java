package com.example.booking.controller;

import com.example.booking.dto.AppointmentRequest;
import com.example.booking.dto.AppointmentResponse;
import com.example.booking.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Appointment booking API")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Book a new appointment")
    public ResponseEntity<AppointmentResponse> book(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse body = service.book(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    @Operation(summary = "List all appointments")
    public List<AppointmentResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by id")
    public AppointmentResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/by-email")
    @Operation(summary = "List appointments for a customer email")
    public List<AppointmentResponse> byEmail(@RequestParam String email) {
        return service.findByEmail(email);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an appointment")
    public AppointmentResponse cancel(@PathVariable Long id) {
        return service.cancel(id);
    }
}