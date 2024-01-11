package com.fsd08.MediLink.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.entity.Appointment;
import com.fsd08.MediLink.entity.Doctor_schedule;
import com.fsd08.MediLink.repository.AppointmentRepository;
import com.fsd08.MediLink.repository.Doctor_scheduleRepository;
import com.fsd08.MediLink.repository.UserRepository;

@Service
public class VideoChatService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final Doctor_scheduleRepository doctor_scheduleRepository;

    public VideoChatService(UserRepository userRepository, AppointmentRepository appointmentRepository,
            Doctor_scheduleRepository doctor_scheduleRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctor_scheduleRepository = doctor_scheduleRepository;
    }

    public String getPatientName(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            User patient = userRepository.findById(appointment.getPatient_id());
            return patient.getUsername();
        }
        return null;
    }

    public String getDoctorName(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            Doctor_schedule doctor_schedule = doctor_scheduleRepository.findById(appointment.getDoctor_schedule_id())
                    .orElse(null);
            if (doctor_schedule != null) {
                User doctor = userRepository.findById(doctor_schedule.getDoctor_id());
                return doctor.getUsername();
            }
        }
        return null;
    }
}
