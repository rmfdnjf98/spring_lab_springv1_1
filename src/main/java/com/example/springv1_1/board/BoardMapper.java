package com.example.springv1_1.board;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    Board findById(int id);

    List<Board> findAll();

    void save(Board board);

    void delete(Board board);

    void update(Board board);
}
