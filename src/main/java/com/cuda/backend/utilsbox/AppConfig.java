package com.cuda.backend.utilsbox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cuda.backend.repository.TweetRepository;
import com.cuda.backend.repository.TweetRepositoryImplementor;
import com.cuda.backend.repository.UserRepository;
import com.cuda.backend.repository.UserRepositoryImplementor;
import com.cuda.backend.service.TweetService;
import com.cuda.backend.service.TweetServiceImplementor;
import com.cuda.backend.service.UserService;
import com.cuda.backend.service.UserServiceImplementor;

import jakarta.persistence.EntityManager;

@Configuration
public class AppConfig {
   
    @Bean("singleton")
    public UserRepository userRepository(EntityManager entityManager){
        return new UserRepositoryImplementor(entityManager);
    }

    @Bean("singleton")
    public TweetRepository tweetRepository(EntityManager entityManager){
        return new TweetRepositoryImplementor(entityManager);
    }

    @Bean("prototype")
    public UserService userService(){
        return new UserServiceImplementor();
    }

    @Bean("prototype")
    public TweetService tweetService(){
        return new TweetServiceImplementor();
    }
}
