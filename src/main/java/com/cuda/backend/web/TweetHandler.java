package com.cuda.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;
import com.cuda.backend.services.TweetService;


@RestController
@RequestMapping(path = "/tweets")
public class TweetHandler {
   
    @Autowired 
    private TweetService tweetService;
    
    // @GetMapping(path = "/read/{tweetId}")
    // public Tweet getById(@PathVariable Long tweetId){
    //     Optional<Tweet> tweet = tweetService.read(tweetId);

    //     if(tweet.isEmpty()){
    //         throw new RecordNotFoundException("tweet not found");
    //     }else{
    //         return tweet.get();
    //     }
    // }

    // @PostMapping(path = "/create")
    // public Tweet createTweet(@Valid @RequestBody Tweet tweet,HttpServletRequest request){
    //     Long authorId = 0L;
    //     return tweetService.save(tweet,authorId);
    // }
    
    // @DeleteMapping(path = "/deleteTweet/{tweetId}")
    // public void delete(@PathVariable Long tweetId) {
    // 	tweetService.delete(tweetId);
    // }
    
    // @PostMapping(path = "/like_tweet/{tweet_id}")
    // public void likeTweet(@PathVariable(name = "tweet_id")Long tweetId){
    //     Long userId = 0L;
    //     tweetService.likeTweet(tweetId,userId);
    // }

    // @PostMapping(path = "/dislike_tweet/{tweet_id}")
    // public void dislikeTweet(@PathVariable(name = "tweet_id")Long tweetId){
    //     Long userId = 0L;
    //     tweetService.removeLike(tweetId,userId);
    // }


    // @GetMapping(path = "/tweet_replies/{tweet_id}/{page_count}")
    // public List<Tweet> getTweetReplies(@PathVariable(name = "tweet_id") Long tweetId,
    //         @PathVariable(name = "page_count") int pageCount){
    //     return tweetService.getTweetReplies(tweetId,pageCount);
    // }

    // @GetMapping(path = "/usersLikedTweet/{tweet_id}/{page_count}")
    // public List<User> getUsersWhoLikedTweet(@PathVariable(name = "tweet_id")Long tweetId,@PathVariable(name = "page_count")int pageCount){
    //     return tweetService.getUsersWhoLikedTweet(tweetId,pageCount);
    // }

    // @GetMapping(path = "/userTweets/mostLiked/{userId}/{pageCount}")
    // public List<Tweet> getUserTweetsMostLiked(@PathVariable Long userId,@PathVariable(name = "page_count")int pageCount){
    //     return tweetService.getUserTweetsMostLiked(userId,pageCount);
    // }

    // @GetMapping(path = "/userTweets/newest/{userId}/{pageCount}")
    // public List<Tweet> getUserTweetsNewest(@PathVariable Long userId,@PathVariable int pageCount){
    //     return tweetService.getUserTweetsNewest(userId,pageCount);
    // }

    // @GetMapping(path = "/userTweets/oldest/{userId}/{pageCount}")
    // public List<Tweet> getUserTweetsOldest(@PathVariable Long userId,@PathVariable int pageCount){
    //     return tweetService.getUserTweetsOldest(userId,pageCount);
    // }

    // @DeleteMapping(path = "/deleteTweets")
    // public void deleteTweetsByIds(@RequestBody List<Long> tweetIds){
    //     tweetService.deleteByIds(tweetIds);
    // }
}
