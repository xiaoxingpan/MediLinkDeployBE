package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.dto.AppointmentsDTO;
import com.fsd08.MediLink.dto.BaseResult;
import com.fsd08.MediLink.dto.PatientDTO;
import com.fsd08.MediLink.dto.ScheduleDTO;
import com.fsd08.MediLink.entity.Appointment;
import com.fsd08.MediLink.entity.Schedule;
import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.service.AppointmentsService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequestMapping("/appointments")
@RestController
public class AppointmentsController {

    @Resource
    private AppointmentsService appointmentsService;

    @GetMapping("/page")
    public Page<AppointmentsDTO> page(int pageSize, int pageNumber, String patientId) {
        return appointmentsService.page(pageSize, pageNumber, patientId);
    }

    @PostMapping("/review")
    public void review(@RequestBody Appointment appointment) {
        appointmentsService.review(appointment);
    }

    @PostMapping("/schedule")
    public BaseResult<?> schedule(@RequestBody ScheduleDTO param) {
        return appointmentsService.schedule(param);
    }

    public List<Appointment> getAppointmentsById(@PathVariable int id) {
        List<Appointment> appointments = appointmentsService.findByPatientId(id);
        return appointments;
    }

    @GetMapping("/getAll")
    public List<AppointmentsDTO> getAllAppointments() {
        return appointmentsService.findAllAppointments();
    }


    @GetMapping("/doctor/schedule")
    public BaseResult<?> getDoctorSchedule(Integer doctorId, String date) {
        List<Schedule> res = appointmentsService.findDoctorSchedule(doctorId, date);
        return BaseResult.ok(res);
    }

    @GetMapping("/users")
    public BaseResult<?> findUsers(int pageSize, int pageNumber, String authority, Integer doctorId) {
        Page<PatientDTO> res = appointmentsService.findUsers(pageSize, pageNumber, authority, doctorId);
        return BaseResult.ok(res);
    }

    @PostMapping("/doctor/update")
    public BaseResult<?> updateDoctor(@RequestBody User param) {
        appointmentsService.updateDoctor(param);
        return BaseResult.ok("success");
    }

    @PostMapping("/update")
    public BaseResult<?> update(@RequestBody Appointment param) {
        appointmentsService.update(param);
        return BaseResult.ok("success");
    }

    @GetMapping("/doctor/patients")
    public BaseResult<?> getDoctorPatients(int pageSize, int pageNumber, Integer doctorId) {
        Page<PatientDTO> res = appointmentsService.findDoctorPatients(pageSize, pageNumber, doctorId);
        return BaseResult.ok(res);
    }

    @GetMapping("/doctor")
    public BaseResult<?> getDoctorInfo(String doctorId) {
        User res = appointmentsService.findDoctorInfo(doctorId);
        return BaseResult.ok(res);
    }

    @PostMapping("/sendAppointmentedEmail")
    public BaseResult<?> sendAppointmentedEmail(Integer appointmentId) {
        appointmentsService.sendAppointmentedEmail(appointmentId);
        return BaseResult.ok("success");
    }

}
