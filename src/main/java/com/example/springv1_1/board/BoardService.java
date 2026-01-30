package com.example.springv1_1.board;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> 게시글목록() {
        List<Board> boards = boardRepository.findAll();
        return boards;
    }

    public Board 게시글상세(Integer id) {
        Board board = boardRepository.findById(id);
        System.out.println("======================");
        System.out.println(board);
        return board;
    }

    @Transactional
    public void 게시글추가(BoardRequest.SaveDTO requestDTO) {
        Board board = new Board();
        board.setTitle(requestDTO.getTitle());
        board.setContent(requestDTO.getContent());
        boardRepository.save(board);

    }

}
