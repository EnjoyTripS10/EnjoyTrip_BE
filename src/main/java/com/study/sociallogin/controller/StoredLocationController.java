package com.study.sociallogin.controller;

import com.study.sociallogin.model.Locations;
import com.study.sociallogin.response.LocationResponse;
import com.study.sociallogin.service.LocationService;
import com.study.sociallogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/location/stored")
@RequiredArgsConstructor
public class StoredLocationController {
    private final LocationService locationService;
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<LocationResponse>> getStoredLocationList(@RequestHeader("Authorization") String token) {
        System.out.println("get stored location list");
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return null;
        }
        return ResponseEntity.ok(locationService.getStoredLocationList(userEmail));
    }
}
