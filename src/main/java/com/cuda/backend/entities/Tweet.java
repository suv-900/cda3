package com.cuda.backend.entities;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Document("tweet")
public class Tweet {
    @Id
    private String id;   
    
    private String tweet;

    private String authorHandle;

    private String imageLocation;

    private Long likes;

    private Set<Tweet> replies;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
}
