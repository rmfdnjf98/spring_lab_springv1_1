package com.example.springv1_1.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        List<Board> boards = boardService.게시글목록();
        request.setAttribute("models", boards);
        return "index";
    }

    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO) {
        boardService.게시글추가(requestDTO);
        return "redirect:/";
    }

    @GetMapping("/boards/{id}")
    public String detail(HttpServletRequest request, @PathVariable("id") Integer id) {
        Board board = boardService.게시글상세(id);
        request.setAttribute("model", board);
        return "board/detail";
    }

    @GetMapping("/board/1/update-form")
    public String updateForm() {
        return "board/udate-form";
    }
}
