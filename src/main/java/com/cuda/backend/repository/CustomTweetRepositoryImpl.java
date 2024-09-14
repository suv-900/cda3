package com.cuda.backend.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;

import jakarta.persistence.Tuple;
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
        
        graph.addAttributeNode("author");
        graph.addAttributeNode("replies");
        graph.addSubGraph("replies").addAttributeNode("author");
        
        Optional<Tweet> tweet = session.byId(Tweet.class).withFetchGraph(graph).loadOptional(tweetId);

        session.close();

        return tweet;
    }
    
    public Tweet readWithPreferences(Long tweetId,Long userId){
        Assert.notNull(userId,"user id cannot be null");
        Assert.notNull(tweetId,"tweet id cannot be null");

        Session session = sessionFactory.openSession();
        Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetId);
        
        if(tweetOptional.isEmpty()){
            session.close();
            throw new RecordNotFoundException();
        }

        User user = new User();
        user.setId(userId);

        Tweet tweet = tweetOptional.get();
        if(tweet.getUserLikes().contains(user)){
            tweet.setLikedByUser(true);
        }
        
        session.close();
        
        return tweet;
    }

    @Transactional
    public Long replyTweet(Long parentTweetId,Tweet replyTweet){
        Assert.notNull(parentTweetId,"parent tweetId cannot be null");

        Session session = sessionFactory.openSession();
        Optional<Tweet> parentTweetOpt = session.byId(Tweet.class).loadOptional(parentTweetId);

        if(parentTweetOpt.isEmpty()){
            throw new RecordNotFoundException("parent tweet doesnt exists");
        }

        Tweet parentTweet = parentTweetOpt.get();
        parentTweet.getReplies().add(replyTweet);
        replyTweet.setParentTweet(parentTweet);
       
        session.persist(replyTweet);
        session.merge(parentTweet);
        session.close();

        return replyTweet.getId();
    }

    @Transactional
    public void likeTweet(Long tweetId,Long userId){
        Assert.notNull(tweetId,"tweet id cannot be null");
        Assert.notNull(userId,"user id cannot be null");
        
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        
        Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetId);
        if(tweetOptional.isEmpty()){
            session.close();
            throw new RecordNotFoundException("tweet doesnt exists.");
        }

        Tweet tweet = tweetOptional.get();
        User user = new User();
        user.setId(userId);
        
        tweet.addUserToLikes(user);
        tweet.increaseLikeCount();

        session.merge(tweet);

        session.flush();
        tc.commit();
        
        session.close();
    }
   
    @Transactional
    public void removeLike(Long tweetId,Long userId){
        Assert.notNull(tweetId,"tweet id cannot be null");
        Assert.notNull(userId,"user id cannot be null");

        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetId);

        if(tweetOptional.isEmpty()){
            session.close();
            throw new RecordNotFoundException("tweet doesnt exists");
        }

        User user = new User();
        user.setId(userId);
        Tweet tweet = tweetOptional.get();
        tweet.removeUserFromLikes(user);
        tweet.decreaseLikeCount();

        session.merge(tweet);
        session.flush();
        tc.commit();

        session.close();
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

        List<Tweet> tweets = query.getResultList();
        session.close();

        return tweets;
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

        List<Tweet> tweets = query.getResultList();
        session.close();

        return tweets;
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

        List<Tweet> tweets = query.getResultList();
        session.close();

        return tweets;
    }

    public List<User> getUsersWhoLikedTweet(Long tweetId,int pageNumber,int pageSize){
        Assert.notNull(tweetId,"tweet id cannot be null");
        Assert.notNull(pageNumber,"page number cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String hql = "select u.id,u.username,u.nickname from Tweet t join t.userLikes u where t.id = :tweetId";
        Session session = sessionFactory.openSession();
        Query<Tuple> query = session.createQuery(hql,Tuple.class);
       
        query.setParameter("tweetId",tweetId);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<Tuple> result = query.getResultList();
        session.close();

        List<User> users = new LinkedList<>();

        for(Tuple tuple : result){
            User user = new User();
            user.setId((Long)tuple.get(0));
            user.setUsername((String)tuple.get(1));
            user.setNickname((String)tuple.get(2));

            users.add(user);
        }
        return users;
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

        List<Tweet> tweets = query.getResultList();
        session.close();

        return tweets;
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
