package com.study.sociallogin.controller;


import com.study.sociallogin.config.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    @GetMapping("/{id}/{message}")
    public String test(@PathVariable String id, @PathVariable String message) throws Exception {
        notificationService.notifyUser(id, message);
        return "send";
    }
}
