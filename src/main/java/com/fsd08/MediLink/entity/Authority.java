package com.fsd08.MediLink.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "authorities")
public class Authority {
    @EmbeddedId
    private AuthorityPK id;

    public Authority() {
    }

    public Authority(AuthorityPK id) {
        this.id = id;
    }

    public Authority(String username, String authority) {
        AuthorityPK id = new AuthorityPK(username, authority);
        this.id = id;
    }

    public AuthorityPK getId() {
        return id;
    }

    public void setId(AuthorityPK id) {
        this.id = id;
    }

}
