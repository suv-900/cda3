package com.cuda.backend.dao;

import com.cuda.backend.entities.Tweet;

public interface TweetDao {
   void add(Tweet tweet)throws Exception;
    
   Tweet view(Long tweetID)throws Exception;

   void update(Tweet tweet)throws Exception;
   
   void delete(Tweet tweet)throws Exception;

   void likeTweet(Long userID)throws Exception;

   void dislikeTweet(Long userID)throws Exception;

   void reportTweet(Long tweetID,Long userID)throws Exception;

   void retweet(Long tweetID,Long userID)throws Exception;
}
