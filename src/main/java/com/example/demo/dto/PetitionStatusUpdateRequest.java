package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class PetitionStatusUpdateRequest {

    @NotBlank(message = "กรุณาระบุสถานะคำร้อง")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
