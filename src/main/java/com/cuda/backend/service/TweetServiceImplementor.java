package com.cuda.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.cuda.backend.entities.Category;
import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.Like;
import com.cuda.backend.entities.User;
import com.cuda.backend.repository.LikeRepository;
import com.cuda.backend.repository.TweetRepository;
import com.cuda.backend.repository.UserRepository;

public class TweetServiceImplementor extends AbstractService implements TweetService{
    
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository tweetLikeRepository;

    public Optional<Tweet> read(Long id){
        return tweetRepository.getByIdLoadGraph(id);
    }

    public Optional<Tweet> read(Long tweetId,Long userId){
        Optional<Tweet> tweetOptional  = tweetRepository.findById(tweetId);

        if(!tweetOptional.isEmpty()){
            Category category = tweetOptional.get().getCategory();
            userRepository.savePreferences(userId,category);
        }
        
        return tweetOptional;
    }

    public List<Tweet> getTweetReplies(Long tweetId,int pageCount){
        return tweetRepository.getTweetReplies(tweetId,pageCount,pageSize);
    }
    
    public List<User> getUsersWhoLikedTweet(Long tweetId,int pageNumber){
        return tweetRepository.getUsersWhoLikedTweet(tweetId, pageNumber, pageNumber);
    }
    
    public void likeTweet(Long tweetId,Long userId){
        tweetRepository.likeTweet(tweetId);
    }

    

    
}

