package com.example.AloneShip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class authService {
    public static void main(String[] args) {
        SpringApplication.run(authService.class,args);
        System.out.println("http://authorization-server:8801/oauth2/authorize?client_id=alone-ship&response_type=code&scope=music&redirect_uri=http://localhost:5173/");
    }
}
