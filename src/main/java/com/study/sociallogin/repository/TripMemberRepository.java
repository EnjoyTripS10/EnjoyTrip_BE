package com.study.sociallogin.repository;

import com.study.sociallogin.model.TripMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface TripMemberRepository extends JpaRepository<TripMembers, Long> {
    List<TripMembers> findAllByTripId(Long id);

    TripMembers findByTripIdAndOwner(Long id, boolean b);

    @Transactional
    void deleteByTripId(Long id);
}
