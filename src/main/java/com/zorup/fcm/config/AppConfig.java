package com.zorup.fcm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    ObjectMapper objectMapperBean(){
        return new ObjectMapper();
    }
}
