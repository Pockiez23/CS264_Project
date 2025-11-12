package com.example.demo.service;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class PetitionService {
    private final PetitionRepository repo;
    private final Path storageDirectory;

    public PetitionService(PetitionRepository repo,
                           @Value("${app.upload.dir:uploads}") String uploadDir){
        this.repo = repo;
        try {
            this.storageDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(this.storageDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("ไม่สามารถสร้างโฟลเดอร์สำหรับเก็บไฟล์คำร้องได้", e);
        }
    }

    @Transactional
    public Petition create(Petition petition, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            storeFile(petition, file);
        }
        if (petition.getStatus() == null || petition.getStatus().isBlank()) {
            petition.setStatus("รอคำพิจารณา");
        }
        return repo.save(petition);
    }

    @Transactional(readOnly = true)
    public Petition get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("ไม่พบคำร้องหมายเลข " + id));
    }

    @Transactional
    public Petition update(Long id, Petition updated, MultipartFile file) throws IOException {
        Petition existing = get(id);

        existing.setAdvisorName(updated.getAdvisorName());
        existing.setCurriculum(updated.getCurriculum());
        existing.setDetails(updated.getDetails());
        existing.setAddress(updated.getAddress());
        existing.setPurpose(updated.getPurpose());
        existing.setEmail(updated.getEmail());
        existing.setMajor(updated.getMajor());
        existing.setPetitionDate(updated.getPetitionDate());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setStudentId(updated.getStudentId());
        existing.setStudentName(updated.getStudentName());

        if (file != null && !file.isEmpty()) {
            deleteExistingFile(existing.getFilePath());
            storeFile(existing, file);
        }

        return repo.save(existing);
    }

    public List<Petition> listByStudent(String studentId){
        return repo.findByStudentIdOrderByUploadDateDesc(studentId);
    }

    private void storeFile(Petition petition, MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String storedFileName = UUID.randomUUID() + extension;
        Path targetLocation = storageDirectory.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        petition.setFileName(originalFilename);
        petition.setFilePath(storedFileName);
    }

    private void deleteExistingFile(String storedFileName) {
        if (storedFileName == null || storedFileName.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(storageDirectory.resolve(storedFileName));
        } catch (IOException ignored) {
            // ถ้าลบไม่สำเร็จให้ข้ามไปก่อนเพื่อไม่ให้การอัปเดตคำร้องล้มเหลว
        }
    }
}
