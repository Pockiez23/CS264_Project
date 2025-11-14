package com.example.demo.service;

import com.example.demo.entity.Petition;
import com.example.demo.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class PetitionService {
    private final PetitionRepository repo;
    private final Path storageDirectory;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "png", "jpg", "jpeg");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            MediaType.APPLICATION_PDF_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE
    );
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9,10}$");

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
            validateFile(file);
            storeFile(petition, file);
        }
        validatePetitionData(petition);
        if (!StringUtils.hasText(petition.getStatus())) {
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

        validatePetitionData(existing);

        if (file != null && !file.isEmpty()) {
            validateFile(file);
            deleteExistingFile(existing.getFilePath());
            storeFile(existing, file);
        }

        return repo.save(existing);
    }

    @Transactional(readOnly = true)
    public List<Petition> listByStudent(String studentId){
        if (!StringUtils.hasText(studentId)) {
            throw new IllegalArgumentException("กรุณาระบุรหัสนักศึกษา");
        }
        return repo.findByStudentIdOrderByUploadDateDesc(studentId);
    }

    @Transactional
    public Petition updateStatus(Long id, String status) {
        Petition existing = get(id);
        if (!StringUtils.hasText(status)) {
            throw new IllegalArgumentException("สถานะคำร้องไม่ถูกต้อง");
        }
        existing.setStatus(status.trim());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Petition existing = get(id);
        deleteExistingFile(existing.getFilePath());
        repo.delete(existing);
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
        if (!StringUtils.hasText(storedFileName)) {
            return;
        }
        try {
            Files.deleteIfExists(storageDirectory.resolve(storedFileName));
        } catch (IOException ignored) {
            // ถ้าลบไม่สำเร็จให้ข้ามไปก่อนเพื่อไม่ให้การอัปเดตคำร้องล้มเหลว
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("ไฟล์มีขนาดใหญ่เกิน 10 MB");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalFilename.substring(dotIndex + 1).toLowerCase();
            }
        }
        String contentType = file.getContentType();
        boolean extensionAllowed = ALLOWED_EXTENSIONS.contains(extension);
        boolean contentTypeAllowed = contentType == null || ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());

        if (!extensionAllowed || !contentTypeAllowed) {
            throw new IllegalArgumentException("อนุญาตเฉพาะไฟล์ PDF, PNG หรือ JPG เท่านั้น");
        }
    }

    private void validatePetitionData(Petition petition) {
        if (petition.getPetitionDate() == null) {
            throw new IllegalArgumentException("กรุณาระบุวันที่ยื่นคำร้อง");
        }
        if (!StringUtils.hasText(petition.getStudentName())) {
            throw new IllegalArgumentException("กรุณาระบุชื่อ-สกุล");
        }
        if (!StringUtils.hasText(petition.getStudentId())) {
            throw new IllegalArgumentException("กรุณาระบุรหัสนักศึกษา");
        }
        if (!StringUtils.hasText(petition.getCurriculum())) {
            throw new IllegalArgumentException("กรุณาระบุหลักสูตร");
        }
        if (!StringUtils.hasText(petition.getMajor())) {
            throw new IllegalArgumentException("กรุณาระบุสาขาวิชา");
        }
        if (!StringUtils.hasText(petition.getAdvisorName())) {
            throw new IllegalArgumentException("กรุณาระบุอาจารย์ที่ปรึกษา");
        }
        if (!StringUtils.hasText(petition.getPhoneNumber()) || !PHONE_PATTERN.matcher(petition.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("กรุณาระบุเบอร์โทรศัพท์ให้ถูกต้อง");
        }
        if (!StringUtils.hasText(petition.getEmail()) || !EMAIL_PATTERN.matcher(petition.getEmail()).matches()) {
            throw new IllegalArgumentException("กรุณาระบุอีเมลให้ถูกต้อง");
        }
        if (!StringUtils.hasText(petition.getAddress())) {
            throw new IllegalArgumentException("กรุณาระบุที่อยู่สำหรับติดต่อ");
        }
        if (!StringUtils.hasText(petition.getPurpose())) {
            throw new IllegalArgumentException("กรุณาระบุความประสงค์ที่ยื่นคำร้อง");
        }
        if (!StringUtils.hasText(petition.getDetails())) {
            throw new IllegalArgumentException("กรุณาระบุรายละเอียดคำร้อง");
        }
    }
}
