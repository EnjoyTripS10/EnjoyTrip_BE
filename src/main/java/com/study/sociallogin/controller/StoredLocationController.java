package com.study.sociallogin.controller;

import com.study.sociallogin.model.Locations;
import com.study.sociallogin.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/location/stored")
@RequiredArgsConstructor
public class StoredLocationController {
    private final LocationService locationService;
    @GetMapping
    public ResponseEntity<List<Locations>> getStoredLocationList() {
        System.out.println("get stored location list");
        String userEmail = "1";
        return ResponseEntity.ok(locationService.getStoredLocationList(userEmail));
    }
}
