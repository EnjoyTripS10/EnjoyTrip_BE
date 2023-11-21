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

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }

    public void transReview(Long id) {
        Trips trip = tripRepository.findByTripId(id);
        trip.setTripType(1);
        tripRepository.save(trip);
    }
}
