package com.cuda.backend.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.cuda.backend.web.UserHandler;

@Configuration
public class SimpleUrlHandlerMappingConfig {
   
    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping(){
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();

        Map<String,Object> map = new HashMap<>();
        map.put("/user",userHandler());

        simpleUrlHandlerMapping.setUrlMap(map);
        return simpleUrlHandlerMapping;
    }


    @Bean
    public UserHandler userHandler(){
        return new UserHandler();
    }
}
