package com.fsd08.MediLink.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "certificates")

public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "description")
    private String description;
    @Column(name = "url")
    private String url;

    public Certificate() {
    }

    public Certificate(int id, int user_id, String description, String url) {
        this.id = id;
        this.userId = user_id;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return userId;
    }

    public void setUser_id(int user_id) {
        this.userId = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
