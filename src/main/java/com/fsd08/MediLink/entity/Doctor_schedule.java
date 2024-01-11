package com.fsd08.MediLink.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "doctor_schedules")
public class Doctor_schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "doctor_id")
    private int doctor_id;
    @Column(name = "schedule_id")
    private int schedule_id;
    @Column(name = "location_id")
    private Integer location_id;
    @Column(name = "date")
    private Date date;
    @Column(name = "price")
    private BigDecimal price;

    public Doctor_schedule() {
    }

    public Doctor_schedule(int id, int doctor_id, int schedule_id, int location_id, Date date, BigDecimal price) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.schedule_id = schedule_id;
        this.location_id = location_id;
        this.date = date;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
