package com.cuda.backend.dao.contracts;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.Tweet;

/**
 *contract for tweet daos. 
 */
public interface TweetDao {
   Long add(Tweet tweet)throws Exception;
    
   Optional<Tweet> view(long tweetID)throws Exception;

   Tweet update(Tweet tweet)throws Exception;
   
   void delete(Tweet tweet)throws Exception;

   void likeTweet(long tweetID)throws Exception;

   void dislikeTweet(long tweetID)throws Exception;

   List<Tweet> getByIds(List<Long> tweetIDs)throws Exception;

   List<Tweet> getUserTweetsOldest(long userID)throws Exception;
   
   List<Tweet> getUserTweetsNewest(long userID)throws Exception;
   
   List<Tweet> getUserTweetsMostLiked(long userID)throws Exception;

   // void reportTweet(Long tweetID,Long userID)throws Exception;

   // void retweet(Long tweetID,Long userID)throws Exception;
}
