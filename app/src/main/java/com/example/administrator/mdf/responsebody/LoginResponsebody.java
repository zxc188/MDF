package com.example.administrator.mdf.responsebody;



public class LoginResponsebody extends Basecord{
    private String session_token;
    private String id;
    private String name;
    private String first_name;
    private String last_name;
    private String email;
    private String is_sys_admin;
    private String last_login_date;
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLast_login_date() {

        return last_login_date;
    }

    public void setLast_login_date(String last_login_date) {
        this.last_login_date = last_login_date;
    }

    public String getIs_sys_admin() {

        return is_sys_admin;
    }

    public void setIs_sys_admin(String is_sys_admin) {
        this.is_sys_admin = is_sys_admin;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession_token() {

        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    @Override
    public String toString() {
        return "LoginResponsebody{" +
                "session_token='" + session_token + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", is_sys_admin='" + is_sys_admin + '\'' +
                ", last_login_date='" + last_login_date + '\'' +
                ", host='" + host + '\'' +
                '}';
    }
}
