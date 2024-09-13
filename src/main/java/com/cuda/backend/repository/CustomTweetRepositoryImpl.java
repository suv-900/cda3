package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;
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

        Session session = sessionFactory.openSession();

        RootGraph<Tweet> graph = session.createEntityGraph(Tweet.class);
        
        log.info("Graph: "+graph.toString());

        graph.addAttributeNode("author");
        graph.addAttributeNode("replies");
        graph.addSubGraph("replies").addAttributeNode("author");
        
        Optional<Tweet> tweet = session.byId(Tweet.class).withFetchGraph(graph).loadOptional(tweetId);
        log.info("Tweet: "+tweet);

        return tweet;
    }
    
    // @Transactional
    // public Long replyTweet(Long parentTweetId,Tweet replyTweet){
    //     Assert.notNull(parentTweetId,"parent tweetId cannot be null");

    //     Session session = sessionFactory.openSession();
    //     Optional<Tweet> parentTweetOpt = session.byId(Tweet.class).loadOptional(parentTweetId);

    //     if(parentTweetOpt.isEmpty()){
    //         throw new RecordNotFoundException("parent tweet doesnt exists");
    //     }

    //     Tweet parentTweet = parentTweetOpt.get();
    //     parentTweet.getReplies().add(replyTweet);
    //     replyTweet.setParentTweet(parentTweet);
       
    //     session.persist(replyTweet);
    //     session.merge(parentTweet);
        
    //     return replyTweet.getId();
    // }

    @Transactional
    public Long replyTweet(Long parentTweetId,Tweet replyTweet){
        
        Tweet parentTweet = new Tweet();
        parentTweet.setId(parentTweetId);
        replyTweet.setParentTweet(parentTweet);
        
        Session session = sessionFactory.openSession();
        session.persist(replyTweet);
        
        return replyTweet.getId();
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
    public void removeLike(Long tweetID){

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


    public List<Tweet> getUserTweetsMostLiked(Long authorId,int pageCount,int pageSize){
        Assert.notNull(authorId,"tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = "from tweet t where t.author.id = :authorId order by t.likes desc";
        Session session = sessionFactory.openSession();
        Query<Tweet> query = session.createQuery(hql,Tweet.class);
        
        query.setParameter("authorId",authorId);
        query.setFirstResult(pageCount);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public List<Tweet> getUserTweetsOldest(Long authorId,int pageCount,int pageSize){
        Assert.notNull(authorId,"tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = "from tweet t where t.author.id = :authorId order by t.createdAt asc";
        Session session = sessionFactory.openSession();
        Query<Tweet> query = session.createQuery(hql,Tweet.class);

        query.setParameter("authorId",authorId);
        query.setFirstResult(pageCount);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public List<Tweet> getUserTweetsNewest(Long authorId,int pageCount,int pageSize){
        Assert.notNull(authorId,"tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = "from tweet t where t.author.id = :authorId order by t.createdAt desc";
        Session session = sessionFactory.openSession();
        Query<Tweet> query = session.createQuery(hql,Tweet.class);

        query.setParameter("authorId",authorId);
        query.setFirstResult(pageCount);
        query.setMaxResults(pageSize);

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

    public List<Tweet> getTweetReplies(Long parentTweetId,int pageCount,int pageSize){
        Assert.notNull(parentTweetId,"parent tweet id cannot be null");
        Assert.notNull(pageCount,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = "from tweet t where t.parentTweet.id = :parentTweetId order by t.likes desc";
        Session session = sessionFactory.openSession();
        Query<Tweet> query = session.createQuery(hql,Tweet.class);

        query.setParameter("parentTweetId",parentTweetId);
        query.setFirstResult(pageCount);
        query.setMaxResults(pageSize);

        return query.getResultList();
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
