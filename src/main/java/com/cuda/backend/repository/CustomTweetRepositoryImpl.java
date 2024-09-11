package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CustomTweetRepositoryImpl implements CustomTweetRepository{
    private final int batchSize = 5;

    @Autowired
    private SessionFactory sessionFactory;

    public Optional<Tweet> getByIdLoadGraph(Long tweetId){
        Assert.notNull(tweetId,"tweet id cannot be null");

        Session session = sessionFactory.getCurrentSession();

        RootGraph<Tweet> graph = session.createEntityGraph(Tweet.class);
    
        graph.addAttributeNode("user");
        graph.addAttributeNode("replies");
        graph.addSubGraph("replies").addAttributeNode("user");
        
        Optional<Tweet> tweet = session.byId(Tweet.class).withFetchGraph(graph).loadOptional(tweetId);

        return tweet;
    }

    
    @Transactional
    public void addTweetReply(Long parentTweetId,Tweet replyTweet){

    }

    @Transactional
    public void likeTweet(Long tweetID){

        Assert.notNull(tweetID,"tweet id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetID);

        if(tweetOptional.isEmpty()){
            throw new EntityNotFoundException();
        }

        Tweet tweet = tweetOptional.get();
        tweet.increaseLikes();

        session.merge(tweet);

    }
    
    @Transactional
    public void dislikeTweet(Long tweetID){

        Assert.notNull(tweetID,"tweet id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetID);

        if(tweetOptional.isEmpty()){
            throw new EntityNotFoundException();
        }

        Tweet tweet = tweetOptional.get();
        tweet.decreaseLikes();

        session.merge(tweet);

    }

    @Transactional
    public List<Tweet> getUserTweetsOldest(Long userID){
        Assert.notNull(userID,"user id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        String hql = "from Tweets t where t.user.id = ? order by created_at asc";
        SelectionQuery<Tweet> query = session.createSelectionQuery(hql,Tweet.class);
            query.setParameter(0,userID);
        
        return query.getResultList();
        
    }

    @Transactional
    public List<Tweet> getUserTweetsNewest(Long userID){
        Assert.notNull(userID,"user id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        String hql = "from Tweet t where t.user.id = ? order by created_at desc";
        SelectionQuery<Tweet> query = session.createSelectionQuery(hql,Tweet.class);
            query.setParameter(0,userID);
        
        return query.getResultList();
        
    }
    
    @Transactional
    public List<Tweet> getUserTweetsMostLiked(Long userID){
        Assert.notNull(userID,"user id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        String hql = "from Tweet t where t.user.id = ? order by t.likes desc";
        SelectionQuery<Tweet> query = session.createSelectionQuery(hql,Tweet.class);
            query.setParameter(0,userID);
        
        return query.getResultList();
        
    }
  
    public List<User> getUsersWhoLikedTweet(Long tweetId,int pageNumber,int pageSize){
        Assert.notNull(tweetId,"tweet id cannot be null");
        Assert.notNull(pageNumber,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String sqlString = "select u.id,u.name from users u join likes l on u.id = l.user_id where l.tweet_id = ?";
        Session session = sessionFactory.openSession();
        Query<User> query = session.createNativeQuery(sqlString,User.class);
        
        query.setParameter(0,tweetId);
        query.setFirstResult(pageNumber);
        query.setFetchSize(pageSize);

        return query.getResultList();

    }

    //implement aspects
    public List<Tweet> getTweetReplies(Long parentTweetId,int pageNumber,int pageSize){
        Assert.notNull(parentTweetId,"parent tweet id cannot be null");
        Assert.notNull(pageNumber,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String sqlString = "select t.tweet_id,t.tweet,t.likes,t.view_count,t.created_at,u.id,u.name from users u join tweets t on u.id = t.user_id where t.parent_tweet_id = ?";
        Session session = sessionFactory.openSession();
        
        List<Tweet> tweetReplies = session.createQuery(sqlString,Tweet.class).getResultList();

        return tweetReplies;
    }
    
    public void insertInBatch(Iterable<Tweet> tweets){
        Session session = sessionFactory.openSession();
        int count = 0;
        for(Tweet tweet : tweets){
            if(count % batchSize == 0 ){
                session.flush();
                session.clear();
            }
            
            session.persist(tweet);
            count++; 
        }
        session.flush();
    }
}
