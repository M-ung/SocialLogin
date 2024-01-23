package com.example.goormoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}

// https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=3d83704a506c101d079ccb4927d62bce&redirect_uri=http://localhost:8081/mainPage&response_type=code
// kauth.kakao.com/oauth/authorize?client_id=3d83704a506c101d079ccb4927d62bce&redirect_uri=http://localhost:8081/mainPage&response_type=code