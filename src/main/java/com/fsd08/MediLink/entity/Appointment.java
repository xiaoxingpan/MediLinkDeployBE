package com.fsd08.MediLink.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "appointments")
public class Appointment {
    public enum Appointment_Status {
        DONE,
        NOSHOW,
        CANCEL,
        CONFIRMED,
        PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "patient_id")
    private Integer patient_id;
    @Column(name = "doctor_schedule_id")
    private Integer doctor_schedule_id;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Appointment_Status status;
    @Column(name = "payment_reference")
    private String payment_reference;
    @Column(name = "medical_record")
    private String medical_record;
    @Column(name = "prescription")
    private String prescription;
    @Column(name = "rate")
    @ColumnDefault("0")
    private Integer rate=0;
    @Column(name = "comment")
    private String comment;

    @Column(name = "type")
    private String type;

    @Column(name = "document")
    private String document;

    public Appointment() {
    }

    public Appointment(int id, int patient_id, int doctor_schedule_id, Appointment_Status status,
                       String payment_reference, String medical_record, String prescription, int rate, String comment, String type) {
        this.id = id;
        this.patient_id = patient_id;
        this.doctor_schedule_id = doctor_schedule_id;
        this.status = status;
        this.payment_reference = payment_reference;
        this.medical_record = medical_record;
        this.prescription = prescription;
        this.rate = rate;
        this.comment = comment;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getDoctor_schedule_id() {
        return doctor_schedule_id;
    }

    public void setDoctor_schedule_id(int doctor_schedule_id) {
        this.doctor_schedule_id = doctor_schedule_id;
    }

    public Appointment_Status getStatus() {
        return status;
    }

    public void setStatus(Appointment_Status status) {
        this.status = status;
    }

    public String getPayment_reference() {
        return payment_reference;
    }

    public void setPayment_reference(String payment_reference) {
        this.payment_reference = payment_reference;
    }

    public String getMedical_record() {
        return medical_record;
    }

    public void setMedical_record(String medical_record) {
        this.medical_record = medical_record;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocument() { return document; }

    public void setDocument(String document) { this.document = document; }
}
