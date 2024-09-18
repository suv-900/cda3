package com.cuda.backend.repository;

public interface CustomLikeRepository {
   public void delete(Long tweetId,Long userId);
   public boolean likeExists(Long tweetId,Long userId); 
}
