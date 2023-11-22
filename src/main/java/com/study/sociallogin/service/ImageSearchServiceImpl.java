package com.study.sociallogin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageSearchServiceImpl implements ImageSearchService {
    @Value("${api.img.naver.id}")
    private String id;
    @Value("${api.img.naver.key}")
    private String secret;

    @Override
    public String searchImage(String query) {
        String url = "https://openapi.naver.com/v1/search/image?query=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", id);
        headers.set("X-Naver-Client-Secret", secret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return extractFirstImageUrl(response.getBody());
    }
    @Override
    public String extractFirstImageUrl(String jsonBody) {
        Pattern pattern = Pattern.compile("\"link\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonBody);

        if (matcher.find()) {
            return matcher.group(1).replace("\\/", "/");
        }
        return "No image found";
    }
}
