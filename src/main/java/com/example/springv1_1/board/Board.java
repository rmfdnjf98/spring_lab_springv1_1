package com.example.springv1_1.board;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Board {

    private Integer id;
    private String title;
    private String content;
    private Timestamp createAt;
}
