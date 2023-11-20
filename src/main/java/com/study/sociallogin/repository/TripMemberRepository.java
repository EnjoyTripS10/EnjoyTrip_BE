package com.study.sociallogin.repository;

import com.study.sociallogin.model.TripMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripMemberRepository extends JpaRepository<TripMembers, Long> {
    List<TripMembers> findAllByTripId(Long id);
}
