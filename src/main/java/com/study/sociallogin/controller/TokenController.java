package com.study.sociallogin.controller;

import com.study.sociallogin.dto.SocialAuthResponse;
import com.study.sociallogin.dto.UserResponse;
import com.study.sociallogin.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {
    @GetMapping("/{accessToken}")
    public void geToken(@PathVariable("accessToken") String accessToken, HttpSession session) {
        System.out.println(session.getAttribute("accessToken"));
        System.out.println("//"+accessToken);
    }
}
