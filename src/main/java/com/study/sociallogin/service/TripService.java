package com.study.sociallogin.service;

import com.study.sociallogin.model.Trips;
import com.study.sociallogin.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {
    private final TripRepository tripRepository;
    public Trips createTrip(Trips trip) {
        return tripRepository.save(trip);
    }

    public Trips getTrip(Long id) {
        return tripRepository.findByTripId(id);
    }

    public List<Trips> getTripList() {
        return tripRepository.findAll();
    }
}
