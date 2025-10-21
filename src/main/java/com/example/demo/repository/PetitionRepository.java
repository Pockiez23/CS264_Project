package com.example.demo.repository;

import com.example.demo.entity.Petition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
    List<Petition> findByStudentIdOrderByUploadDateDesc(String studentId);
}
