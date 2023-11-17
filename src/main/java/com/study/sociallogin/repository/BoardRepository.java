package com.study.sociallogin.repository;

import com.study.sociallogin.model.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Boards, Long> {
    Boards findByBoardId(Long id);

//    void updateBoardHit(Long );
}
