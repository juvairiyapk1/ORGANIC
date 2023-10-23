package com.timeco.application.model.user;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CustomSession {

    @Id
    private String id;
    private  String session;

    public CustomSession(String id, String session) {
        this.id = id;
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
