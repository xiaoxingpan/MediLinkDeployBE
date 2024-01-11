package com.fsd08.MediLink.dto;

import lombok.Getter;

import java.math.BigDecimal;


public class UserDto {

    @Getter
    private int id;

    @Getter
    private String username;

    @Getter
    private String name;

    @Getter
    private String avatar;

    @Getter
    private boolean isSuspended;

    @Getter
    private String authority;

    @Getter
    private String departmentName;

    @Getter
    private String description;

    @Getter
    private BigDecimal price;

    @Getter
    private Boolean approved;

    @Getter
    private String email;

    public UserDto() {}


    public UserDto(String email, int id, String username, String avatar, boolean isSuspended, String authority, String departmentName, String description, BigDecimal price, String name, boolean approved) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
        this.isSuspended = isSuspended;
        this.authority = authority;
        this.departmentName = departmentName;
        this.description = description;
        this.price = price;
        this.approved = approved;
        this.email =email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setPrice(BigDecimal price) {

        
        this.price = price;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
