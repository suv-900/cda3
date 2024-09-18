package com.cuda.backend.repository;

import java.util.List;

import com.cuda.backend.entities.dto.TweetDTO;
import com.cuda.backend.entities.dto.UserDTO;

public interface CustomTweetRepository {
    TweetDTO read(Long tweetId);

    void increaseLikeCount(Long tweetID);

    void decreaseLikeCount(Long tweetID);

    List<TweetDTO> getTweetReplies(Long parentTweetId,int pageCount,int pageSize);

    List<UserDTO> getUsersWhoLikedTweet(Long tweetId,int pageCount,int pageSize);

    List<TweetDTO> getUserTweetsOldest(Long userID,int pageCount,int pageSize);

    List<TweetDTO> getUserTweetsNewest(Long userID,int pageCount,int pageSize);

    List<TweetDTO> getUserTweetsMostLiked(Long userID,int pageCount,int pageSize);
}