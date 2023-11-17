package com.study.sociallogin.repository;

import com.study.sociallogin.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
}
