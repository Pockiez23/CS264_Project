package com.example.demo.controller;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petitions")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"}) // พอร์ต front-end ของคุณ
public class PetitionController {

    @Autowired
    private PetitionRepository petitionRepository;

    @PostMapping("/upload")
    public ResponseEntity<Petition> uploadPetition(@RequestBody Petition petition) {
        // ป้องกัน null
        if (petition.getStatus() == null || petition.getStatus().isEmpty()) {
            petition.setStatus("รอคำพิจารณา");
        }

        Petition saved = petitionRepository.save(petition);
        return ResponseEntity.ok(saved);
    }



    @GetMapping
    public ResponseEntity<?> getAllPetitions() {
        return ResponseEntity.ok(petitionRepository.findAll());
    }
}
