package com.cuda.backend.repository.beans;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cuda.backend.dao.contracts.TweetDao;
import com.cuda.backend.dao.contracts.UserDao;
import com.cuda.backend.entities.Tweet;
import com.cuda.backend.repository.contracts.TweetRepository;

@Repository
public class TweetRepositoryBean implements TweetRepository{
   private TweetDao tweetDao;
   
   private UserDao userDao;

   public TweetRepositoryBean(TweetDao tweetDao,UserDao userDao){
      this.tweetDao = tweetDao;
      this.userDao = userDao;
   }
   
   // public TweetRepositoryImpl(){}
   //set properites of the object called by the container
   //optional dependencies
   // public void setTweetDao(TweetDao tweetDao){
   //    this.tweetDao = tweetDao;
   // }

   
   // @Bean produces a bean to be managed by spring container
   public void add(Tweet tweet)throws Exception{
      tweetDao.add(tweet);
   }

   public Optional<Tweet> view(long tweetID)throws Exception{
      return tweetDao.view(tweetID);
   }

   public Tweet update(Tweet tweet)throws Exception{
      return tweetDao.update(tweet);
   }

   public void delete(Tweet tweet)throws Exception{
      tweetDao.delete(tweet);
   }

   public void likeTweet(long tweetID)throws Exception{
      tweetDao.likeTweet(tweetID);
   }
   
   public void dislikeTweet(long tweetID)throws Exception{
      tweetDao.dislikeTweet(tweetID);
   }

   public List<Tweet> getUserTweetsNewest(long userID)throws Exception{
      return tweetDao.getUserTweetsNewest(userID);
   }
   
   public List<Tweet> getUserTweetsOldest(long userID)throws Exception{
      return tweetDao.getUserTweetsOldest(userID);
   }

   public List<Tweet> getUserTweetsMostLiked(long userID)throws Exception{
      return tweetDao.getUserTweetsMostLiked(userID);
   }

   
}
