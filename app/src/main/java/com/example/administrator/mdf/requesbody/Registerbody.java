package com.example.administrator.mdf.requesbody;

/*
* 注册请求体
* */

public class Registerbody {
    private String email;
    private String first_name;
    private String last_name;
    private String display_name;
    private String new_password;
    private String code="String";

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getDisplay_name() {

        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getLast_name() {

        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {

        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
