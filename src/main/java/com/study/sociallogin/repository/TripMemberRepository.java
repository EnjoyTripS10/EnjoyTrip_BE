package com.study.sociallogin.repository;

import com.study.sociallogin.model.TripMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripMemberRepository extends JpaRepository<TripMembers, Long> {
}
