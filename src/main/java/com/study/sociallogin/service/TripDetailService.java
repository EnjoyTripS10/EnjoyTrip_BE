package com.study.sociallogin.service;

import com.study.sociallogin.model.TripDetails;
import com.study.sociallogin.repository.TripDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripDetailService {

    private final TripDetailRepository tripDetailRepository;
        public TripDetails createLocation(TripDetails details) {
            return tripDetailRepository.save(details);
    }
}
