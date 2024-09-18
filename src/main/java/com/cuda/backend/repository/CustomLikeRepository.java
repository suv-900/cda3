package com.cuda.backend.repository;

import java.util.List;

import com.cuda.backend.entities.dto.TweetDTO;

public interface CustomLikeRepository {
   public void delete(Long tweetId,Long userId);
   
   public boolean likeExists(Long tweetId,Long userId);
   
   public List<TweetDTO> checkUserReactions(Long userId,List<TweetDTO> tweets);
}
