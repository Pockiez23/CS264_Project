package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "petitions")
public class Petition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advisor_name")
    private String advisorName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    private String status;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "petition_date")
    private LocalDate petitionDate;

    @Column(name = "curriculum")
    private String curriculum;

    @Column(name = "major")
    private String major;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "details", columnDefinition = "NVARCHAR(MAX)")
    private String details;

    @Column(name = "address", columnDefinition = "NVARCHAR(MAX)")
    private String address;

    @Column(name = "purpose", columnDefinition = "NVARCHAR(MAX)")
    private String purpose;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "file_data")
    @JsonIgnore
    private byte[] fileData;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
        if (status == null || status.isBlank()) {
            status = "รอคำพิจารณา";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        uploadDate = LocalDateTime.now();
        if (status == null || status.isBlank()) {
            status = "รอคำพิจารณา";
        }
    }

    // ✅ Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public LocalDate getPetitionDate() { return petitionDate; }
    public void setPetitionDate(LocalDate petitionDate) { this.petitionDate = petitionDate; }

    public String getCurriculum() { return curriculum; }
    public void setCurriculum(String curriculum) { this.curriculum = curriculum; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getFileContentType() { return fileContentType; }
    public void setFileContentType(String fileContentType) { this.fileContentType = fileContentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
