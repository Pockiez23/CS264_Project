package com.example.demo.service;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PetitionService {
    private final PetitionRepository repo;
    public PetitionService(PetitionRepository repo){ this.repo = repo; }

    @Transactional
    public Petition save(Petition p){
        // ใส่ business validation ได้ เช่น ตรวจขนาดไฟล์/จำนวนไฟล์ (ถ้าเก็บ)
        return repo.save(p);
    }

    public List<Petition> listByStudent(String studentId){
        return repo.findByStudentIdOrderByUploadDateDesc(studentId);
    }
}
