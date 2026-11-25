package com.example.booking.service;

import com.example.booking.entity.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendConfirmation(Appointment apt) {
        log.info("[MOCKED EMAIL] To={}, Subject='Appointment Confirmed', Id={}, Time={}, Status={}",
                apt.getCustomerEmail(), apt.getId(), apt.getAppointmentTime(), apt.getStatus());
    }

    public void sendCancellation(Appointment apt) {
        log.info("[MOCKED EMAIL] To={}, Subject='Appointment Cancelled', Id={}",
                apt.getCustomerEmail(), apt.getId());
    }
}