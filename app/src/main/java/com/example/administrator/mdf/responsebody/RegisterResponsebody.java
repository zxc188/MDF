package com.example.administrator.mdf.responsebody;

/*
* 注册接口的返回Responsebody
* */

public class RegisterResponsebody {
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "RegisterResponsebody{" +
                "success='" + success + '\'' +
                '}';
    }
}

