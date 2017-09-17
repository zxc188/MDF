package com.example.administrator.mdf.requesbody;

/*
* 登录请求体
* */

public class Loginbody {
    private String email;
    private String password;
    private String duration = "0";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
