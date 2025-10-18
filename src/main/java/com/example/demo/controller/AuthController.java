package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"}) // ✅ อนุญาตให้ frontend เข้าถึง
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String url = "https://restapi.tu.ac.th/api/v1/auth/Ad/verify";

            // ✅ สร้าง body ตามที่ TU API ต้องการ
            Map<String, String> body = new HashMap<>();
            body.put("UserName", request.getUserName());
            body.put("PassWord", request.getPassWord());

            // ✅ Header ต้องใส่ Application-Key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Application-Key", "YOUR_TU_APPLICATION_KEY"); // 🔑 ใส่คีย์จริงตรงนี้

            // ✅ ยิงไปยัง TU API
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // ✅ ถ้าสำเร็จ
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> data = response.getBody();

                // แปลง response JSON → DTO
                LoginResponse result = new LoginResponse();
                result.setStatus((Boolean) data.get("status"));
                result.setMessage((String) data.get("message"));
                result.setUsername((String) data.get("username"));
                result.setDisplayname_th((String) data.get("displayname_th"));
                result.setDisplayname_en((String) data.get("displayname_en"));
                result.setEmail((String) data.get("email"));
                result.setTu_status((String) data.get("tu_status"));
                result.setType((String) data.get("type"));
                result.setDepartment((String) data.get("department"));
                result.setFaculty((String) data.get("faculty"));

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(401)
                        .body(new LoginResponse(false, "ไม่สามารถตรวจสอบบัญชีได้"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new LoginResponse(false, "เกิดข้อผิดพลาดภายในระบบ: " + e.getMessage()));
        }
    }
}
