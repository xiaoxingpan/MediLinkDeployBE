package com.fsd08.MediLink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fsd08.MediLink.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = "select s.* from doctor_schedules ds left join schedules s on ds.schedule_id = s.id where ds.doctor_id = ? and ds.date = ?", nativeQuery = true)
    List<Schedule> selectByDoctorIdAndDate(Integer doctorId, String date);

}
