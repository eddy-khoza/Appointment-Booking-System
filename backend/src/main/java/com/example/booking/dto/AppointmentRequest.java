package com.example.booking.dto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class AppointmentRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 120)
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    @Size(max = 180)
    private String customerEmail;

    @NotNull(message = "Appointment time is required")
    @Future(message = "Appointment time must be in the future")
    private LocalDateTime appointmentTime;

    @Size(max = 255)
    private String notes;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}