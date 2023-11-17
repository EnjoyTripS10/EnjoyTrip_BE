package com.study.sociallogin.controller;

import com.study.sociallogin.dto.SocialLoginRequest;
import com.study.sociallogin.dto.LoginResponse;
import com.study.sociallogin.dto.UserResponse;
import com.study.sociallogin.model.User;
import com.study.sociallogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/social-login")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
        System.out.println("hello");
        return ResponseEntity.created(URI.create("/social-login"))
                .body(userService.doSocialLogin(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
        System.out.print("get id/" + id + "//");
        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }

    @GetMapping("/findmember/{email}")
    public ResponseEntity<UserResponse> getUserEmail(@PathVariable("email") String email) {
        System.out.print("get id/" + email + "//");
        UserResponse userResponse = userService.getUserEmail(email);

        if(userResponse == null){
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(userResponse);
    }
}
