package com.study.sociallogin.controller;

import com.study.sociallogin.config.NotificationService;
import com.study.sociallogin.model.Boards;
import com.study.sociallogin.model.Comments;
import com.study.sociallogin.request.CommentRequest;
import com.study.sociallogin.request.TripRequest;
import com.study.sociallogin.response.CommentResponse;
import com.study.sociallogin.service.BoardService;
import com.study.sociallogin.service.CommentService;
import com.study.sociallogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/board/comment")
@RequiredArgsConstructor
public class CommentController {
    private final UserService userService;
    private final BoardService boardService;
    private final NotificationService notificationService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<HttpStatus> registeComment(@RequestHeader("Authorization") String token,
                                                     @RequestBody CommentRequest commentRequest) throws Exception {
        String userEmail = userService.getUserEmailFromToken(token);
        //Long boardId = Long.parseLong(id);
        if(userEmail == null){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }
        Boards board = boardService.getBoardId(commentRequest.getBoardId());

        if(board==null){
            return (ResponseEntity<HttpStatus>) ResponseEntity.badRequest();
        }

        Comments comment = Comments.builder()
                .boardId(board.getBoardId())
                .commentContent(commentRequest.getContent())
                .userEmail(userEmail)
                .build();

        comment = commentService.createComment(comment);

        String message = userEmail+"님이 ["+board.getBoardTitle() + "]에 댓글을 작성했습니다.";
        notificationService.notifyUser(board.getUserEmail(), message);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public  ResponseEntity<List<CommentResponse>> getList(@RequestHeader("Authorization") String token,
                                                          @PathVariable("id") Long boardId){
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return null;
        }
        Boards board = boardService.getBoardId(boardId);
        if(board ==null){
            ResponseEntity.badRequest();
        }
        return commentService.getList(boardId,userEmail);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteComment(@RequestHeader("Authorization") String token,
                                                     @PathVariable("id") Long commentId) throws Exception {
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }
        Comments comment = commentService.getCommentId(commentId);
        if(comment == null){
            return ResponseEntity.badRequest().build();
        }
        if(!comment.getUserEmail().equals(userEmail)){
            return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
