package com.cuda.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.repository.TweetRepository;

@Service
public class TweetServiceImpl extends AbstractService implements TweetService{
    
    @Autowired
    private TweetRepository tweetRepository;


    public Tweet save(Tweet tweet){
        return tweetRepository.save(tweet);
    }
    
    public Optional<Tweet> read(Long id){
        return tweetRepository.getByIdLoadGraph(id);
    }

    public Tweet update(Tweet tweet){
        return save(tweet); 
    }

    public void delete(Tweet tweet){
        tweetRepository.delete(tweet);
    }

    public void deleteById(Long tweetId){
        tweetRepository.deleteById(tweetId);
    }

    public void deleteByIds(List<Long> tweetIds){
        tweetRepository.deleteAllById(tweetIds);
    }

    public void likeTweet(Long tweetId,Long userId){
        tweetRepository.likeTweet(tweetId);
    }
    
    public void removeLike(Long tweetId,Long userId){
        tweetRepository.removeLike(tweetId);
    }

    public Long replyTweet(Long parentTweetId,Tweet tweet){
        return tweetRepository.replyTweet(parentTweetId,tweet);
    }
    
    public Optional<Tweet> read(Long tweetId,Long userId){
        Optional<Tweet> tweetOptional  = tweetRepository.findById(tweetId);

        if(!tweetOptional.isEmpty()){
            // Category category = tweetOptional.get().getCategory();
            // userRepository.savePreferences(userId,category);
        }
        
        return tweetOptional;
    }

    public List<Tweet> getTweetReplies(Long tweetId,int pageCount){
        return tweetRepository.getTweetReplies(tweetId,pageCount,pageSize);
    }
    
    public List<User> getUsersWhoLikedTweet(Long tweetId,int pageNumber){
        return tweetRepository.getUsersWhoLikedTweet(tweetId, pageNumber, pageNumber);
    }
    
    
    public List<Tweet> getUserTweetsMostLiked(Long userId,int pageCount){
        return tweetRepository.getUserTweetsMostLiked(userId,pageCount,pageSize);
    }

    public List<Tweet> getUserTweetsOldest(Long userId,int pageCount){
        return tweetRepository.getUserTweetsOldest(userId,pageCount,pageSize);
    }
    
    public List<Tweet> getUserTweetsNewest(Long userId,int pageCount){
        return tweetRepository.getUserTweetsNewest(userId,pageCount,pageSize);
    }
    
}

