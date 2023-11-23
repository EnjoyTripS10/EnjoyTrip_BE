package com.study.sociallogin.repository;

import com.study.sociallogin.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByBoardId(Long boardId);

    Comments findByCommentId(Long commentId);
}
