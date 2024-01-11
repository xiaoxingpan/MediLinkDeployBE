package com.fsd08.MediLink.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsd08.MediLink.entity.Doctor_schedule;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface Doctor_scheduleRepository extends JpaRepository<Doctor_schedule, Integer> {

    @Query(value = "SELECT\n" +
            "\tds.* \n" +
            "FROM\n" +
            "\tdoctor_schedules ds\n" +
            "\tLEFT JOIN schedules s ON ds.schedule_id = s.id \n" +
            "WHERE\n" +
            "\tds.doctor_id = :doctorId \n" +
            "\tAND ds.date = :date \n" +
            "\tAND :startTime BETWEEN s.start_time and s.end_time", nativeQuery = true)
    Doctor_schedule findBySchedule(Integer doctorId, String date, String startTime);

}
