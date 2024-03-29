package com.study.sociallogin.controller;

import com.study.sociallogin.dto.BoardResponse;
import com.study.sociallogin.model.Boards;
import com.study.sociallogin.service.BoardLikeService;
import com.study.sociallogin.service.BoardService;
import com.study.sociallogin.service.LocationService;
import com.study.sociallogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/boardLiked")
@RequiredArgsConstructor
public class BoardLikeController {
    @Value("${imageFile.uploadPath}")
    private String filePath;
    private final BoardService boardService;
    private final LocationService locationService;
    private final BoardLikeService boardLikeService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getLikeBoardList(@RequestHeader("Authorization") String token) {
        System.out.println("get liked board list");
        //login check
        String userEmail = userService.getUserEmailFromToken(token);
        if(userEmail == null){
            return null;
        }

        List<Boards> boards = boardService.findBoardsLikedByUser(userEmail);
        List<BoardResponse> responses = new ArrayList<>();
        for (Boards board: boards) {
            BoardResponse boardRespose = BoardResponse.builder()
                    .boardId(board.getBoardId())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .createdAt(board.getCreatedAt().toString())
                    .boardHit(board.getBoardHit())
                    .userEmail(board.getUserEmail())
                    .locationId(board.getLocationId())
                    .build();
            try {
                String folderPath = filePath +"/" + board.getUserEmail() + "/" + board.getBoardId();

                Path dirPath = Paths.get(folderPath);
                DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);
                for (Path filePath : stream) {
                    if (Files.isRegularFile(filePath)) {
                        // 첫 번째 이미지 파일을 찾았으므로 읽어서 반환
                        byte[] imageBytes = Files.readAllBytes(filePath);
                        boardRespose.setImage(imageBytes);
                        responses.add(boardRespose);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<BoardResponse>> getLikeSearchList(@PathVariable("keyword") String keyword) {
        System.out.println("get liked board list");
        String userEmail = "1";

        List<Boards> boards = boardService.findSearchBoardsLikedByUser(userEmail, keyword);
        List<BoardResponse> responses = new ArrayList<>();
        for (Boards board: boards) {
            BoardResponse boardRespose = BoardResponse.builder()
                    .boardId(board.getBoardId())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .createdAt(board.getCreatedAt().toString())
                    .boardHit(board.getBoardHit())
                    .userEmail(board.getUserEmail())
                    .locationId(board.getLocationId())
                    .build();
            try {
                String folderPath = filePath +"/" + board.getUserEmail() + "/" + board.getBoardId();

                Path dirPath = Paths.get(folderPath);
                DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);
                for (Path filePath : stream) {
                    if (Files.isRegularFile(filePath)) {
                        // 첫 번째 이미지 파일을 찾았으므로 읽어서 반환
                        byte[] imageBytes = Files.readAllBytes(filePath);
                        boardRespose.setImage(imageBytes);
                        responses.add(boardRespose);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(responses);
    }

}
