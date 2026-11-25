package com.example.booking.repository;

import com.example.booking.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByCustomerEmailOrderByAppointmentTimeDesc(String customerEmail);
}