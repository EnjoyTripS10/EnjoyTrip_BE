package com.study.sociallogin.repository;

import com.study.sociallogin.model.TripDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripDetailRepository extends JpaRepository<TripDetails, Long> {
    List<TripDetails> findByTripIdAndTripDateOrderByOrderIndexAsc(Long tripId, int tripDate);
}
