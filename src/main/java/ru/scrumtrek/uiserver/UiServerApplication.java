package ru.scrumtrek.uiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.scrumtrek.uiserver"})
public class UiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiServerApplication.class, args);
    }

}

