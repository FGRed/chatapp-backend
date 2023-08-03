package net.chatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatappBackendApplication {


    public static void main(String[] args) {
        System.out.println(args[0]);
        SpringApplication.run(ChatappBackendApplication.class, args);
    }


}
