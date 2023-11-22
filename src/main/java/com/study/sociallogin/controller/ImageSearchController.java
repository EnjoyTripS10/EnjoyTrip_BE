package com.study.sociallogin.controller;

import com.study.sociallogin.service.ImageSearchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ImageSearchController {

    private final ImageSearchServiceImpl imageSearchService;
    @GetMapping
    public String searchImage(@RequestParam String query) {
        return imageSearchService.searchImage(query);
    }
}
