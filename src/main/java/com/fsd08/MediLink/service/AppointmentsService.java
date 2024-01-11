package com.fsd08.MediLink.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.fsd08.MediLink.dto.AppointmentsDTO;
import com.fsd08.MediLink.dto.BaseResult;
import com.fsd08.MediLink.dto.PatientDTO;
import com.fsd08.MediLink.dto.ScheduleDTO;
import com.fsd08.MediLink.dto.UserDto;
import com.fsd08.MediLink.entity.Appointment;
import com.fsd08.MediLink.entity.Schedule;
import com.fsd08.MediLink.entity.User;

public interface AppointmentsService {

    Page<AppointmentsDTO> page(int pageSize, int pageNumber, String patientId);

    void review(Appointment appointment);

    void setDefaultReview();

    BaseResult<?> schedule(ScheduleDTO param);
    List<AppointmentsDTO> findAllAppointments();

    List<Appointment> findByPatientId(int id);

    List<Schedule> findDoctorSchedule(Integer doctorId, String date);

    Page<PatientDTO> findUsers(int pageSize, int pageNumber, String authority, Integer doctorId);

    void updateDoctor(User param);

    void update(Appointment param);

    Page<PatientDTO> findDoctorPatients(int pageSize, int pageNumber, Integer doctorId);

    User findDoctorInfo(String doctorId);

    void sendAppointmentedEmail(Integer appointmentId);

}
