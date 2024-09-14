package com.cuda.backend.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;
import com.cuda.backend.services.TweetService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/tweets")
public class TweetHandler {
   
    @Autowired 
    private TweetService tweetService;
    
    @GetMapping(path = "/readTweet")
    public Tweet readTweet(@NotNull @RequestParam Long tweetId){
        Optional<Tweet> tweet = tweetService.read(tweetId);

        if(tweet.isEmpty()){
            throw new RecordNotFoundException("tweet not found");
        }else{
            return tweet.get();
        }
    }

    @GetMapping(path = "/readTweetWithPreferences")
    public Tweet readTweetWithPreferences(@NotNull @RequestParam Long tweetId,@NotNull @RequestParam Long userId){
        return tweetService.readWithPreferences(tweetId,userId);
    }

    @PostMapping(path = "/createTweet")
    public Tweet createTweet(@Valid @RequestBody Tweet tweet,@NotNull @RequestParam Long authorId){
        User author = new User();
        author.setId(authorId);
        
        tweet.setAuthor(author);
        
        return tweetService.save(tweet);
    }
    
    @DeleteMapping(path = "/deleteTweet")
    public void delete(@NotNull @RequestParam Long tweetId) {
    	tweetService.deleteById(tweetId);
    }
    
    @PostMapping(path = "/likeTweet")
    public void likeTweet(@NotNull @RequestParam Long tweetId,@NotNull @RequestParam Long userId){
        tweetService.likeTweet(tweetId,userId);
    }

    @PostMapping(path = "/dislikeTweet")
    public void dislikeTweet(@NotNull @RequestParam Long tweetId,@NotNull @RequestParam Long userId){
        tweetService.removeLike(tweetId,userId);
    }

    @PostMapping(path = "/addReplyTweet")
    public Long addReplyTweet(@Valid @RequestBody Tweet replyTweet,@NotNull @RequestParam Long parentTweetId,@NotNull @RequestParam Long authorId){
        return tweetService.replyTweet(parentTweetId,authorId,replyTweet);
    }

    @GetMapping(path = "/tweetReplies")
    public List<Tweet> getTweetReplies(@NotNull @RequestParam Long tweetId,@NotNull @RequestParam int pageCount){
        return tweetService.getTweetReplies(tweetId,pageCount);
    }

    @GetMapping(path = "/usersLikedTweet")
    public List<User> getUsersWhoLikedTweet(@NotNull @RequestParam Long tweetId,@NotNull @RequestParam int pageCount){
        return tweetService.getUsersWhoLikedTweet(tweetId,pageCount);
    }

    @GetMapping(path = "/userTweets/mostLiked")
    public List<Tweet> getUserTweetsMostLiked(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return tweetService.getUserTweetsMostLiked(userId,pageCount);
    }

    @GetMapping(path = "/userTweets/newest")
    public List<Tweet> getUserTweetsNewest(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return tweetService.getUserTweetsNewest(userId,pageCount);
    }
    
    @GetMapping(path = "/userTweets/oldest")
    public List<Tweet> getUserTweetsOldest(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return tweetService.getUserTweetsOldest(userId,pageCount);
    }
    
    @DeleteMapping(path = "/deleteTweets")
    public void deleteTweetsByIds(@RequestBody List<Long> tweetIds){
        tweetService.deleteByIds(tweetIds);
    }

    @GetMapping(path = "/getAll")
    public List<Tweet> getAllTweets(@NotNull @RequestParam int pageCount){
        return tweetService.getAll(pageCount);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/deleteAll")
    public void deleteAll(){
        tweetService.deleteAll();
    }
}
