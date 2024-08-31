package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;

public interface TweetRepository extends JpaRepository<Tweet,Long>{

    int batchSize = 10;
    // Tweet view(Long tweetID,Long userID)throws Exception;
    Optional<Tweet> getByIdLoadGraph(Long tweetId);

    void likeTweet(Long tweetID);

    void dislikeTweet(Long tweetID);

    List<Tweet> getUserTweetsOldest(Long userID);

    List<Tweet> getUserTweetsNewest(Long userID);

    List<Tweet> getUserTweetsMostLiked(Long userID);

    @Query(
        value = "SELECT * FROM Tweets t WHERE t.parent IS NOT NULL AND t.parent = ?1 ORDER BY t.likes DESC OFFSET = ?2 LIMIT = ?3",
        nativeQuery = true)
    List<Tweet> getTweetReplies(Long parentTweetId,int offset,int limit);
    // void reportTweet(Long tweetID,Long userID)throws Exception;

    List<User> getUsersWhoLikedTweet(Long tweetId,int pageNumber,int pageSize);
}

