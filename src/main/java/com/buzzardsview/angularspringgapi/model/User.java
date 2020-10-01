package com.buzzardsview.angularspringgapi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @Size(max = 22)
    private String googleId;
    private String name;
    private String picture;

    public User(@Size(max = 22) String googleId, String name, String picture) {
        this.googleId = googleId;
        this.name = name;
        this.picture = picture;
    }

    public User() {
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
