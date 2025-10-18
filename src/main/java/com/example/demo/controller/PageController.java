package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        // ให้เปิด login.html เป็นหน้าแรก
        return "redirect:/html/login.html";
    }
}
