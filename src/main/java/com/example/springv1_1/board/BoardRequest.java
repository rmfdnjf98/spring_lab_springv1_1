package com.example.springv1_1.board;

import lombok.Data;

public class BoardRequest {

    @Data
    public static class SaveDTO {
        private String title;
        private String content;
    }

    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
    }

}
