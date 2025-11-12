package com.example.demo.controller;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import com.example.demo.service.PetitionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/petitions")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"}) // ถ้าหน้าเว็บวิ่งคนละพอร์ต
public class PetitionController {

    private final PetitionService service;
    private final PetitionRepository petitionRepository;

    public PetitionController(PetitionService service, PetitionRepository petitionRepository) {
        this.service = service;
        this.petitionRepository = petitionRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Petition> uploadPetition(
            @RequestParam("petitionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate petitionDate,
            @RequestParam("studentName") String studentName,
            @RequestParam("studentId") String studentId,
            @RequestParam("curriculum") String curriculum,
            @RequestParam("major") String major,
            @RequestParam("advisorName") String advisorName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("email") String email,
            @RequestParam("details") String details,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        Petition petition = new Petition();
        petition.setAdvisorName(advisorName);
        petition.setCurriculum(curriculum);
        petition.setDetails(details);
        petition.setEmail(email);
        petition.setMajor(major);
        petition.setPetitionDate(petitionDate);
        petition.setPhoneNumber(phoneNumber);
        petition.setStudentId(studentId);
        petition.setStudentName(studentName);

        Petition saved = service.create(petition, file);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Petition>> getAllPetitions() {
        return ResponseEntity.ok(petitionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Petition> getPetition(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Petition>> myPetitions(@RequestParam String studentId){
        return ResponseEntity.ok(service.listByStudent(studentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Petition> updatePetition(
            @PathVariable Long id,
            @RequestParam("petitionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate petitionDate,
            @RequestParam("studentName") String studentName,
            @RequestParam("studentId") String studentId,
            @RequestParam("curriculum") String curriculum,
            @RequestParam("major") String major,
            @RequestParam("advisorName") String advisorName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("email") String email,
            @RequestParam("details") String details,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        Petition petition = new Petition();
        petition.setAdvisorName(advisorName);
        petition.setCurriculum(curriculum);
        petition.setDetails(details);
        petition.setEmail(email);
        petition.setMajor(major);
        petition.setPetitionDate(petitionDate);
        petition.setPhoneNumber(phoneNumber);
        petition.setStudentId(studentId);
        petition.setStudentName(studentName);

        Petition updated = service.update(id, petition, file);
        return ResponseEntity.ok(updated);
    }
}
