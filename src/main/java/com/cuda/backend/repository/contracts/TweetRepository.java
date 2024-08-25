package com.cuda.backend.repository.contracts;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.Tweet;

public interface TweetRepository{
    void add(Tweet tweet)throws Exception;
    
    //anon view
    Optional<Tweet> view(long tweetID)throws Exception;

    //custom view
    // Tweet view(Long tweetID,Long userID)throws Exception;

    Tweet update(Tweet tweet)throws Exception;

    void delete(Tweet tweet)throws Exception;

    List<Tweet> getUserTweetsOldest(long userID)throws Exception;

    List<Tweet> getUserTweetsNewest(long userID)throws Exception;

    List<Tweet> getUserTweetsMostLiked(long userID)throws Exception;

    // void reportTweet(Long tweetID,Long userID)throws Exception;
}
