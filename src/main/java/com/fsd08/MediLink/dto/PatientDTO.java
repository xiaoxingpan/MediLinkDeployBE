package com.fsd08.MediLink.dto;

import java.math.BigDecimal;

public interface PatientDTO {
    
    public Integer getId();

    public String getUsername();

    public Boolean getEnabled();

    public String getName();

    public String getAddress();

    public String getTelephone();

    public String getEmail();

    public String getAvatar();

    public String getDescription();

    public String getPostal();

    public Integer getDepartment_id();

    public Boolean getApproved();

    public Boolean getSuspended();

    public BigDecimal getDefault_price();

    public String getAuthority();

    public String getMedical_record();

    public String getPrescription();

    public String getAppointmentId();

    public String getDocument();

    public Integer getCertificateId();

    public String getCertificate();

}
