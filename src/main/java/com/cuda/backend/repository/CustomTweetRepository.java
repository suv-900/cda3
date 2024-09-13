package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;

public interface CustomTweetRepository {
    Optional<Tweet> getByIdLoadGraph(Long tweetId);
   
    void likeTweet(Long tweetID);

    void removeLike(Long tweetID);

    Long replyTweet(Long parentTweetId,Tweet replyTweet);

    List<Tweet> getTweetReplies(Long parentTweetId,int pageCount,int pageSize);
    // void reportTweet(Long tweetID,Long userID)throws Exception;

    List<User> getUsersWhoLikedTweet(Long tweetId,int pageCount,int pageSize);

    List<Tweet> getUserTweetsOldest(Long userID,int pageCount,int pageSize);

    List<Tweet> getUserTweetsNewest(Long userID,int pageCount,int pageSize);

    List<Tweet> getUserTweetsMostLiked(Long userID,int pageCount,int pageSize);

}