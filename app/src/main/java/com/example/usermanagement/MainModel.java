package com.example.usermanagement;

public class MainModel {
    String name, course, email, turl;
    Long stcode;

    MainModel() {}
    public MainModel(String name, String course, String email, String turl, Long stcode) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.stcode = stcode;
        this.turl = turl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public Long getStcode() {
        return stcode;
    }

    public void setStcode(Long stcode) {
        this.stcode = stcode;
    }
}
