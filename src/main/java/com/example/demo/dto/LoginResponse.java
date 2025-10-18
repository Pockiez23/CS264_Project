package com.example.demo.dto;

public class LoginResponse {
    private boolean status;
    private String message;
    private String username;
    private String displayname_th;
    private String displayname_en;
    private String email;
    private String tu_status;
    private String type;
    private String department;
    private String faculty;

    // Default constructor
    public LoginResponse() {}

    // Constructor custom (เวลา error หรือ success)
    public LoginResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getter/Setter ทั้งหมด
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDisplayname_th() { return displayname_th; }
    public void setDisplayname_th(String displayname_th) { this.displayname_th = displayname_th; }

    public String getDisplayname_en() { return displayname_en; }
    public void setDisplayname_en(String displayname_en) { this.displayname_en = displayname_en; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTu_status() { return tu_status; }
    public void setTu_status(String tu_status) { this.tu_status = tu_status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
}
