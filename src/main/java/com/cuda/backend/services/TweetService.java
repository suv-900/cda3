package com.cuda.backend.services;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;

public interface TweetService {
   Optional<Tweet> read(Long id);
   
   // Optional<Tweet> read(Long id,Long userId);
   
   Tweet save(Tweet tweet);

   Tweet update(Tweet tweet);

   void delete(Tweet tweet);

   void deleteById(Long tweetId);

   void deleteByIds(List<Long> tweetIds);

   void likeTweet(Long tweetId,Long userId);
   
   void removeLike(Long tweetId,Long userId);

   Long replyTweet(Long rootTweetId,Tweet replyTweet);

   // void bookMarkTweet(Long tweetId,Long userId);

   List<Tweet> getTweetReplies(Long tweetId,int pageCount);

   List<User> getUsersWhoLikedTweet(Long tweetId,int pageCount);
   
   // List<Tweet> generateHomeFeed(Long userId,int pageCount);
  
   List<Tweet> getUserTweetsMostLiked(Long userId,int pageCount);

   List<Tweet> getUserTweetsOldest(Long userId,int pageCount);

   List<Tweet> getUserTweetsNewest(Long userId,int pageCount);

   // List<Tweet> getTrendingTweetsToday(int pageCount);

   // List<Tweet> getTrendingTweetsThisWeek(int pageCount);
}
