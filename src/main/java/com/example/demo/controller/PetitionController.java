package com.example.demo.controller;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petitions")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class PetitionController {

    @Autowired
    private PetitionRepository petitionRepository;

    // บันทึกคำร้อง
    @PostMapping("/submit")
    public ResponseEntity<Petition> submitPetition(@RequestBody Petition petition) {
        Petition saved = petitionRepository.save(petition);
        return ResponseEntity.ok(saved);
    }
    
    @PostMapping("/upload")
    public Petition uploadPetition(@RequestBody Petition petition) {
        petition.setStatus("รอดำเนินการ");
        petition.setUploadDate(java.time.LocalDateTime.now());
        return petitionRepository.save(petition);
    }

    // ดึงคำร้องทั้งหมด
    @GetMapping
    public List<Petition> getAllPetitions() {
        return petitionRepository.findAll();
    }

    // ลบคำร้อง
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetition(@PathVariable Long id) {
        petitionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
