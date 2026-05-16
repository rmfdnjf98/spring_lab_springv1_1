DROP TABLE IF EXISTS board_tb;

CREATE TABLE board_tb (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(100),
    content   TEXT,
    create_at TIMESTAMP
);
