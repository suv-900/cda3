package com.cuda.backend.entities.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TweetDTO {
    Long id;
    String tweet;
    UserDTO author;
    Long likeCount;
    Long viewCount;
    List<TweetDTO> replies = new ArrayList<>();
    Timestamp updatedAt; 
}
