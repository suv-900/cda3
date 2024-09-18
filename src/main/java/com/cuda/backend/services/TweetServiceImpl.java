package com.cuda.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cuda.backend.entities.Like;
import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.entities.dto.TweetDTO;
import com.cuda.backend.entities.dto.UserDTO;
import com.cuda.backend.exceptions.RecordNotFoundException;
import com.cuda.backend.repository.LikeRepository;
import com.cuda.backend.repository.TweetRepository;

import jakarta.transaction.Transactional;

@Service
public class TweetServiceImpl extends AbstractService implements TweetService{
    
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private LikeRepository likeRepository;

    public Tweet save(Tweet tweet){
        return tweetRepository.save(tweet);
    }
    
    public TweetDTO read(Long tweetId){
        TweetDTO tweet = tweetRepository.read(tweetId);
        if(tweet == null){
            throw new RecordNotFoundException("tweet not found");
        }else{
            return tweet;
        }
    }

    public TweetDTO readWithPreferences(Long tweetId,Long userId){
        TweetDTO tweet = read(tweetId);
        
        if(likeRepository.likeExists(tweetId,userId)){
            tweet.setLiked(true);
        }

        return tweet;
    }

    public Tweet update(Tweet tweet){
        return save(tweet); 
    }

    public void delete(Tweet tweet){
        tweetRepository.delete(tweet);
    }

    public void deleteAll(){
        tweetRepository.deleteAll();
    }
    public void deleteById(Long tweetId){
        tweetRepository.deleteById(tweetId);
    }

    public void deleteByIds(List<Long> tweetIds){
        tweetRepository.deleteAllById(tweetIds);
    }

    @Transactional
    public void likeTweet(Long tweetId,Long userId){
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        User user = new User();
        user.setId(userId);

        Like like = new Like();
        like.setTweet(tweet);
        like.setUser(user);

        likeRepository.save(like);
        tweetRepository.increaseLikeCount(tweetId);
    }
    
    @Transactional
    public void removeLike(Long tweetId,Long userId){
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        User user = new User();
        user.setId(userId);

        Like like = new Like();
        like.setTweet(tweet);
        like.setUser(user);

        likeRepository.delete(tweetId,userId);
        tweetRepository.decreaseLikeCount(tweetId);
    }
    
    public Long replyTweet(Long parentTweetId,Long authorId,Tweet replyTweet){
        Tweet parentTweet = new Tweet();
        parentTweet.setId(parentTweetId);
        
        User author = new User();
        author.setId(authorId);
        
        replyTweet.setParentTweet(parentTweet);
        replyTweet.setAuthor(author);

        save(replyTweet);

        return replyTweet.getId();
    }
    
    public Optional<Tweet> read(Long tweetId,Long userId){
        Optional<Tweet> tweetOptional  = tweetRepository.findById(tweetId);

        if(!tweetOptional.isEmpty()){
            // Category category = tweetOptional.get().getCategory();
            // userRepository.savePreferences(userId,category);
        }
        
        return tweetOptional;
    }

    public List<TweetDTO> getTweetReplies(Long tweetId,int pageCount){
        return tweetRepository.getTweetReplies(tweetId,pageCount,pageSize);
    }
    
    public List<UserDTO> getUsersWhoLikedTweet(Long tweetId,int pageCount){
        return tweetRepository.getUsersWhoLikedTweet(tweetId, pageCount, pageSize);
    }
    
    
    public List<TweetDTO> getUserTweetsMostLiked(Long userId,int pageCount){
        return tweetRepository.getUserTweetsMostLiked(userId,pageCount,pageSize);
    }

    public List<TweetDTO> getUserTweetsOldest(Long userId,int pageCount){
        return tweetRepository.getUserTweetsOldest(userId,pageCount,pageSize);
    }
    
    public List<TweetDTO> getUserTweetsNewest(Long userId,int pageCount){
        return tweetRepository.getUserTweetsNewest(userId,pageCount,pageSize);
    }
    
    public List<Tweet> getAll(int pageCount){
        Pageable pageable = PageRequest.of(pageCount,pageSize);
        Page<Tweet> page = tweetRepository.findAll(pageable);
        return page.getContent();
    }
}

