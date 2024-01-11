package com.fsd08.MediLink.service.impl;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;

import com.fsd08.MediLink.dto.UserDto;
import com.fsd08.MediLink.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.fsd08.MediLink.dto.AppointmentsDTO;
import com.fsd08.MediLink.dto.BaseResult;
import com.fsd08.MediLink.dto.PatientDTO;
import com.fsd08.MediLink.dto.ScheduleDTO;
import com.fsd08.MediLink.entity.Appointment;
import com.fsd08.MediLink.entity.Doctor_schedule;
import com.fsd08.MediLink.entity.Schedule;
import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.repository.AppointmentRepository;
import com.fsd08.MediLink.repository.Doctor_scheduleRepository;
import com.fsd08.MediLink.repository.ScheduleRepository;
import com.fsd08.MediLink.repository.UserRepository;
import com.fsd08.MediLink.service.AppointmentsService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    @Value("${appointment.mail.host:smtp-mail.outlook.com}")
    private String mailHost;
    @Value("${appointment.mail.port:587}")
    private Integer mailPort;
    @Value("${appointment.mail.username:fsd08medilink@outlook.com}")
    private String mailUsername;
    @Value("${appointment.mail.password:Pw123456}")
    private String mailPassword;

    private JavaMailSender mailSender;

    @Resource
    private AppointmentRepository appointmentRepository;
    @Resource
    private Doctor_scheduleRepository doctorScheduleRepository;
    @Resource
    private ScheduleRepository scheduleRepository;
    @Resource
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        // init mail sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        // Enable STARTTLS
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.starttls.required", "true");
        mailSender.setJavaMailProperties(mailProperties);

        this.mailSender = mailSender;
    }

    @Override
    public Page<AppointmentsDTO> page(int pageSize, int pageNumber, String patientId) {
        PageRequest page = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "ds.date");
        return appointmentRepository.findPage(page, patientId);
    }

    @Override
    public void review(Appointment appointment) {
        Appointment exist = appointmentRepository.getReferenceById(appointment.getId());
        exist.setComment(appointment.getComment());
        exist.setRate(appointment.getRate());
        appointmentRepository.save(exist);
    }

    @Transactional
    @Override
    public void setDefaultReview() {
        appointmentRepository.setDefaultReview(5);
    }

    @Transactional
    @Override
    public BaseResult<?> schedule(ScheduleDTO param) {
        Doctor_schedule scheduled = doctorScheduleRepository.findBySchedule(param.getDoctorId(), param.getDate(), param.getStartTime());
        if (Objects.nonNull(scheduled)) {
            return BaseResult.error("The doctor has already made an appointment");
        }

        Schedule schedule = new Schedule();
        LocalTime startTime = LocalTime.parse(param.getStartTime());
        schedule.setStart_time(Time.valueOf(startTime));
        schedule.setEnd_time(Time.valueOf(startTime.plusHours(1)));
        schedule = scheduleRepository.save(schedule);

        Doctor_schedule doctorSchedule = new Doctor_schedule();
        doctorSchedule.setSchedule_id(schedule.getId());
        doctorSchedule.setDoctor_id(param.getDoctorId());
        doctorSchedule.setDate(Date.valueOf(param.getDate()));
        doctorSchedule = doctorScheduleRepository.save(doctorSchedule);

        Appointment appointment = new Appointment();
        appointment.setDoctor_schedule_id(doctorSchedule.getId());
        appointment.setPatient_id(param.getPatientId());
        appointment.setStatus(Appointment.Appointment_Status.PENDING);
        appointment.setType(param.getType());
        appointmentRepository.save(appointment);

        return BaseResult.ok("The appointment was successful");
    }

    @Override
    public List<Appointment> findByPatientId(int id) {
        return appointmentRepository.findByPatient_id(id);
    }

    @Override
    public List<AppointmentsDTO> findAllAppointments() {
        return appointmentRepository.findAllAppointmentsWithDetails();
    }

    @Override
    public List<Schedule> findDoctorSchedule(Integer doctorId, String date) {
        return scheduleRepository.selectByDoctorIdAndDate(doctorId, date);
    }

    @Override
    public Page<PatientDTO> findUsers(int pageSize, int pageNumber, String authority, Integer doctorId) {
        PageRequest page = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "id");
        return userRepository.findPage(page, authority);
    }

    @Override
    public void updateDoctor(User param) {
        if (Objects.isNull(param.getId())) {
            return;
        }
        User exist = userRepository.findById(param.getId());
        if (Objects.isNull(exist)) {
            return;
        }
        Optional.ofNullable(param.getSchedules()).ifPresent(exist::setSchedules);
        Optional.ofNullable(param.getDefault_price()).ifPresent(exist::setDefault_price);
        Optional.ofNullable(param.isApproved()).ifPresent(exist::setApproved);
        Optional.ofNullable(param.isSuspended()).ifPresent(exist::setSuspended);
        if(Objects.isNull(param.getAuthority())){
            Optional.of(param.isEnabled()).ifPresent(exist::setEnabled);

        }
        //Optional.of(param.isEnabled()).ifPresent(exist::setEnabled);
        userRepository.save(exist);
    }

    @Override
    public void update(Appointment param) {
        if (Objects.isNull(param.getId())) {
            return;
        }
        Appointment exist = appointmentRepository.findById(param.getId()).get();
        if (Objects.isNull(exist)) {
            return;
        }

        Optional.ofNullable(param.getPayment_reference()).ifPresent(exist::setPayment_reference);
        Optional.ofNullable(param.getMedical_record()).ifPresent(exist::setMedical_record);
        Optional.ofNullable(param.getPrescription()).ifPresent(exist::setPrescription);
        Optional.ofNullable(param.getStatus()).ifPresent(exist::setStatus);
        Optional.ofNullable(param.getDocument()).ifPresent(exist::setDocument);
        appointmentRepository.save(exist);
    }

    @Override
    public Page<PatientDTO> findDoctorPatients(int pageSize, int pageNumber, Integer doctorId) {
        PageRequest page = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "date");
        return appointmentRepository.findDoctorPatients(page, doctorId);
    }

    @Override
    public User findDoctorInfo(String doctorId) {
        return userRepository.findById(doctorId).get();
    }

    @Override
    public void sendAppointmentedEmail(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        User patient = userRepository.findById(appointment.getPatient_id());

        //
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(patient.getEmail());
        message.setSubject("Appointment confirmation");
        message.setText("Your appointment has been confirmed, please pay attention to the time of the appointment");
        mailSender.send(message);
    }

}
