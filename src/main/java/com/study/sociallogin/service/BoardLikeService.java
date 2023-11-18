package com.study.sociallogin.service;

import com.study.sociallogin.repository.BoardLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;

    public boolean getLikeCheck(Long boardId, String userEmail) {
        int cnt =  boardLikeRepository.countByBoardIdAndUserEmail(boardId, userEmail);
        return cnt > 0;
    }

    public int getBoardLikes(Long boardId) {
        return boardLikeRepository.countByBoardId(boardId);
    }

    public void createBoardLike(Long boardId, String userEmail) {
        boolean isExist = getLikeCheck(boardId, userEmail);
        System.out.println("createBoardLike"+isExist);
        if(!isExist)
            boardLikeRepository.save(com.study.sociallogin.model.BoardLikes.builder()
                    .boardId(boardId)
                    .userEmail(userEmail)
                    .build());
    }

    public void removeBoardLike(Long boardId, String userEmail) {
        System.out.println("removeBoardLike");
        boolean isExist = getLikeCheck(boardId, userEmail);
        System.out.println(isExist);
        if(isExist)
            boardLikeRepository.deleteByBoardIdAndUserEmail(boardId, userEmail);
    }
}
