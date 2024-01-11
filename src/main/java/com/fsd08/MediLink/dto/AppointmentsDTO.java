package com.fsd08.MediLink.dto;

import java.sql.Time;

public interface AppointmentsDTO {

    Integer getId();

    Integer getPatientId();

    Integer getDoctorScheduleId();

    String getStatus();

    String getPaymentReference();

    String getMedicalRecord();

    String getPrescription();

    Integer getRate();

    String getComment();

    String getDoctorId();

    String getDate();

    String getDoctorName();

    String getPatientName();

    String getDepartmentName();

    String getDoctorAvatar();

    String getPatientAvatar();

    Time getStartTime();

    Time getEndTime();

    String getLocation();

    String getDocument();

String getType();

}
