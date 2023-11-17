package com.study.sociallogin.repository;

import com.study.sociallogin.model.TripLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripLikeRepository extends JpaRepository<TripLikes, Long> {
}
