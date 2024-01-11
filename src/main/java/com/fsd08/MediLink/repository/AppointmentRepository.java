package com.fsd08.MediLink.repository;

import com.fsd08.MediLink.dto.AppointmentsDTO;
import com.fsd08.MediLink.dto.PatientDTO;
import com.fsd08.MediLink.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT\n" +
            "\ta.id as id,\n" +
            "\ta.patient_id as patientId,\n" +
            "\ta.doctor_schedule_id as doctorScheduleId,\n" +
            "\ta.status as status,\n" +
            "\ta.type as type,\n" +
            "\ta.payment_reference as paymentReference,\n" +
            "\ta.medical_record as medicalRecord,\n" +
            "\ta.prescription as prescription,\n" +
            "\ta.rate as rate,\n" +
            "\ta.document as document,\n" +
            "\ta.comment as comment,\n" +
            "\tds.doctor_id as doctorId,\n" +
            "\tds.date as date,\n" +
            "\ts.start_time as startTime, \n" +
            "\ts.end_time as endTime, \n" +
            "\tu.name as doctorName, \n" +
            "\tup.name as patientName, \n" +
            "\td.department_name as departmentName \n" +
            "FROM\n" +
            "\tAppointment a\n" +
            "\tLEFT JOIN Doctor_schedule ds ON a.doctor_schedule_id = ds.id\n" +
            "\tLEFT JOIN User u ON ds.doctor_id = u.id \n" +
            "\tLEFT JOIN Schedule s ON ds.schedule_id = s.id" +
            "\tLEFT JOIN User up ON a.patient_id = up.id \n" +
            "\tLEFT JOIN Department d ON u.department_id = d.id\n" +
            "WHERE\n" +
            "\t(:patientId IS NULL OR a.patient_id = :patientId)")
    Page<AppointmentsDTO> findPage(Pageable pageable, @Param("patientId") String patientId);

    @Query(value = "UPDATE appointments a,\n" +
            "doctor_schedules ds \n" +
            "SET a.rate = 5 \n" +
            "WHERE\n" +
            "\ta.`status` = 'DONE' \n" +
            "\tAND a.rate IS NULL \n" +
            "\tAND a.doctor_schedule_id = ds.id \n" +
            "\tAND DATEDIFF( NOW(), ds.date ) > :days", nativeQuery = true)
    @Modifying
    void setDefaultReview(int days);

    @Query("SELECT a FROM Appointment a WHERE a.patient_id = :patientId")
    List<Appointment> findByPatient_id(@Param("patientId") Integer patientId);

    @Query(value = "SELECT u.*,a.medical_record,a.prescription,a.id as appointmentId,a.document FROM doctor_schedules ds LEFT JOIN appointments a ON ds.id=a.doctor_schedule_id LEFT JOIN users u ON a.patient_id=u.id WHERE (:doctorId is null or ds.doctor_id=:doctorId)", nativeQuery =  true)
    Page<PatientDTO> findDoctorPatients(Pageable pageable, @Param("doctorId") Integer doctorId);

    @Query("SELECT\n" +
            "\ta.id as id,\n" +
            "\ta.patient_id as patientId,\n" +
            "\ta.doctor_schedule_id as doctorScheduleId,\n" +
            "\ta.status as status,\n" +
            "\ta.payment_reference as paymentReference,\n" +
            "\ta.medical_record as medicalRecord,\n" +
            "\ta.prescription as prescription,\n" +
            "\ta.document as document,\n" +
            "\ta.rate as rate,\n" +
            "\ta.comment as comment,\n" +
            "\ta.type as type,\n" +
            "\tds.doctor_id as doctorId,\n" +
            "\tds.date as date,\n" +
            "\tu.name as doctorName, \n" +
            "\tup.name as patientName, \n" +
            "\td.department_name as departmentName, \n" +
            "\tu.avatar as doctorAvatar, \n" +
            "\tup.avatar as patientAvatar, \n" +
            "\ts.start_time as startTime, \n" +
            "\ts.end_time as endTime, \n" +
            "\tl.name as location \n" +
            "FROM\n" +
            "\tAppointment a\n" +
            "\tLEFT JOIN Doctor_schedule ds ON a.doctor_schedule_id = ds.id\n" +
            "\tLEFT JOIN User u ON ds.doctor_id = u.id \n" +
            "\tLEFT JOIN User up ON a.patient_id = up.id \n" +
            "\tLEFT JOIN Department d ON u.department_id = d.id\n" +
            "\tLEFT JOIN Schedule s ON ds.schedule_id = s.id" +
            "\tLEFT JOIN Location l ON ds.location_id = s.id")
    List<AppointmentsDTO> findAllAppointmentsWithDetails();

}
