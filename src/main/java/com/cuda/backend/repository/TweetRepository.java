package com.cuda.backend.repository;

import java.util.Set;

import com.cuda.backend.entities.Tweet;

public interface TweetRepository{
    void add(Tweet tweet)throws Exception;
    
    //anon view
    Tweet view(Long tweetID)throws Exception;

    //custom view
    Tweet view(Long tweetID,Long userID)throws Exception;

    Tweet update(Tweet tweet)throws Exception;

    void delete(Tweet tweet)throws Exception;

    Set<Tweet> getUserTweetsSortByBest(Long userID)throws Exception;

    Set<Tweet> getUserTweetsSortByNewest(Long userID)throws Exception;

    Set<Tweet> getUserTweetsSortByOldest(Long userID)throws Exception;

    void reportTweet(Long tweetID,Long userID)throws Exception;
}
