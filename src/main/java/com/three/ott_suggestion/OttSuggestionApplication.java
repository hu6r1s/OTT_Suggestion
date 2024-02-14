package com.three.ott_suggestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OttSuggestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(OttSuggestionApplication.class, args);
    }

}
