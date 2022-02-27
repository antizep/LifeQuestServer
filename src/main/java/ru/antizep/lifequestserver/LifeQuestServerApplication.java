package ru.antizep.lifequestserver;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LifeQuestServerApplication {

    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(LifeQuestServerApplication.class, args);
    }

}
