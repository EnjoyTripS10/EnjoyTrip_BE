package com.study.sociallogin.repository;

import com.study.sociallogin.model.Trips;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trips, Long> {
    Trips findByTripId(Long id);
}
