package com.study.sociallogin.service;

import com.study.sociallogin.dto.BoardLocationDto;
import com.study.sociallogin.model.Boards;
import com.study.sociallogin.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public Boards createBoard(Boards board) {
        return boardRepository.save(board);
    }

    public List<Boards> getBoardList() {
        return boardRepository.findAll();
    }

    public Boards getBoardId(Long id) {
        return boardRepository.findByBoardId(id);
    }

    public void updateBoardHit(Long boardId) {
        Boards board = boardRepository.findByBoardId(boardId);
        board.setBoardHit(board.getBoardHit() + 1);
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    public List<Boards> getLikedBoardList(String userEmail) {

        return null;
    }

    public List<Boards> findBoardsLikedByUser(String userEmail) {
        return boardRepository.findBoardsLikedByUser(userEmail);
    }

    public List<Boards> findSearchBoardsLikedByUser(String userEmail, String keyword) {
        return boardRepository.findBoardsLikedByUserWithTitle(userEmail, keyword);
    }

    public List<Boards> findSearchBoards(String keyword) {
        return boardRepository.findBoardsWithTitle(keyword);
    }

    public Boards update(Boards board) {
        return boardRepository.save(board);
    }

    public List<BoardLocationDto> findSearchCityBoards(String keyword) {
        return boardRepository.findBoardsWithLocationName(keyword);
    }
}
