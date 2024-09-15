package com.cuda.backend.services;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.dto.UserDTO;

public interface TweetService {
   Optional<Tweet> read(Long id);
  
   Tweet readWithPreferences(Long tweetId,Long userId);

   // Optional<Tweet> read(Long id,Long userId);
  
   List<Tweet> getAll(int pageCount);
   
   Tweet save(Tweet tweet);

   Tweet update(Tweet tweet);

   void delete(Tweet tweet);

   void deleteAll();

   void deleteById(Long tweetId);

   void deleteByIds(List<Long> tweetIds);

   void likeTweet(Long tweetId,Long userId);
   
   void removeLike(Long tweetId,Long userId);

   Long replyTweet(Long parentTweetId,Long authorId,Tweet replyTweet);

   // void bookMarkTweet(Long tweetId,Long userId);

   List<Tweet> getTweetReplies(Long tweetId,int pageCount);

   List<UserDTO> getUsersWhoLikedTweet(Long tweetId,int pageCount);
   
   // List<Tweet> generateHomeFeed(Long userId,int pageCount);
  
   List<Tweet> getUserTweetsMostLiked(Long userId,int pageCount);

   List<Tweet> getUserTweetsOldest(Long userId,int pageCount);

   List<Tweet> getUserTweetsNewest(Long userId,int pageCount);

   // List<Tweet> getTrendingTweetsToday(int pageCount);

   // List<Tweet> getTrendingTweetsThisWeek(int pageCount);
}
