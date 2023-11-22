package com.study.sociallogin.service;

import org.springframework.stereotype.Service;

@Service
public interface ImageSearchService {
    String searchImage(String query);

    String extractFirstImageUrl(String jsonBody);
}
