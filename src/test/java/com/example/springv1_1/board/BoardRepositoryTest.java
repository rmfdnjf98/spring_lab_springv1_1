package com.example.springv1_1.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import jakarta.persistence.EntityManager;

@Import(BoardRepository.class)
@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void findById_test() {
        int id = 1;

        Board board = boardRepository.findById(id);

        System.out.println("============================");
        System.out.println("board Title : " + board.getTitle());
        System.out.println("board Content : " + board.getContent());
    }

    @Test
    public void findAll() {
        List<Board> boards = boardRepository.findAll();

        System.out.println("==========================");
        System.out.println("Board count : " + boards.size());
        System.out.println("Board 1 title : " + boards.get(0).getTitle());
        System.out.println("Board 2 content : " + boards.get(1).getContent());
    }

    @Test
    public void save_test() {
        Board board = new Board();
        board.setTitle("title3");
        board.setContent("content3");

        boardRepository.save(board);

        List<Board> boards = boardRepository.findAll();
        System.out.println("======================");
        System.out.println("Board count : " + boards.size());
        System.out.println("Board ID : " + boards.get(2).getId());
        System.out.println("Board Title : " + boards.get(2).getTitle());
        System.out.println("Board Content : " + boards.get(2).getContent());
    }

    @Test
    public void update_test() {
        int id = 2;

        Board board = boardRepository.findById(id);
        board.setTitle("title-update");
        board.setContent("content-update");
        em.flush();
        em.clear();

        Board result = boardRepository.findById(id);
        System.out.println("=========================");
        System.out.println("Board title : " + result.getTitle());
        System.out.println("Board content : " + result.getContent());
    }

    @Test
    public void delete_test() {
        int id = 2;

        Board board = boardRepository.findById(id);

        boardRepository.delete(board);
        em.flush();

        List<Board> boards = boardRepository.findAll();
        System.out.println("=======================");
        System.out.println("Board count : " + boards.size());
    }

}
