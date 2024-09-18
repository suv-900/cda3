package com.cuda.backend.entities.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TweetDTO {
    Long id;
    String tweet;
    UserDTO author;
    long likeCount;
    long viewCount;
    LocalDateTime updatedAt;
    boolean liked;
}
