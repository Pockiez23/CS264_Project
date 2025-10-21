package com.example.demo.controller;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import com.example.demo.service.PetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Petition> uploadPetition(@RequestBody Petition petition) {
        if (petition.getStatus() == null || petition.getStatus().isBlank()) {
            petition.setStatus("รอคำพิจารณา");
        }
        Petition saved = service.save(petition);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Petition>> getAllPetitions() {
        return ResponseEntity.ok(petitionRepository.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Petition>> myPetitions(@RequestParam String studentId){
        return ResponseEntity.ok(service.listByStudent(studentId));
    }
}
