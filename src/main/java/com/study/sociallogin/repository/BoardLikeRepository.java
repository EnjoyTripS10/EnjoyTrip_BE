package com.study.sociallogin.repository;

import com.study.sociallogin.model.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface BoardLikeRepository extends JpaRepository<BoardLikes, Long> {
    int countByBoardId(Long boardId);

    int countByBoardIdAndUserEmail(Long boardId, String userEmail);

    @Transactional
    void deleteByBoardIdAndUserEmail(Long boardId, String userEmail);

    @Transactional
    void deleteByBoardId(Long boardId);
}
