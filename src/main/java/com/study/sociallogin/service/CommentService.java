package com.study.sociallogin.service;

import com.study.sociallogin.model.Comments;
import com.study.sociallogin.repository.CommentRepository;
import com.study.sociallogin.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public Comments createComment(Comments comment) {
        return commentRepository.save(comment);
    }

    public ResponseEntity<List<CommentResponse>> getList(Long boardId, String userEmail) {
        List<CommentResponse> result = new ArrayList<>();
        List<Comments> list = commentRepository.findByBoardId(boardId);
        for (Comments comment : list) {
            boolean mine = false;
            if(comment.getUserEmail().equals(userEmail)) mine = true;
            result.add(CommentResponse.builder()
                            .commentId(comment.getCommentId())
                            .commentContent(comment.getCommentContent())
                            .createdAt(comment.getCreatedAt().toString())
                            .userEmail(comment.getUserEmail())
                            .mine(mine)
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    public Comments getCommentId(Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
