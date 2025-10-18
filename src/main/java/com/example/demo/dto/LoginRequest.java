package com.example.demo.dto;

public class LoginRequest {
    private String UserName;
    private String PassWord;

    // Getter/Setter
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
