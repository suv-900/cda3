package com.cuda.backend.entities.dto;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

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
    AtomicLong likeCount;
    AtomicLong viewCount;
    LocalDateTime updatedAt;
}
