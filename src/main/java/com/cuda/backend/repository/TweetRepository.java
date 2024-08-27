package com.cuda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cuda.backend.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet,Long>{

    // Tweet view(Long tweetID,Long userID)throws Exception;

    void likeTweet(Long tweetID);

    void dislikeTweet(Long tweetID);

    List<Tweet> getUserTweetsOldest(Long userID);

    List<Tweet> getUserTweetsNewest(Long userID);

    List<Tweet> getUserTweetsMostLiked(Long userID);

    // void reportTweet(Long tweetID,Long userID)throws Exception;
}

