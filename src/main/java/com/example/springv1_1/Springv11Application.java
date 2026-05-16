package com.example.springv1_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan("com.example.springv1_1.board")
@SpringBootApplication
public class Springv11Application {

	public static void main(String[] args) {
		SpringApplication.run(Springv11Application.class, args);
	}

}
