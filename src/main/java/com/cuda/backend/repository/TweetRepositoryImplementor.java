package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.utilsbox.HibernateBox;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Repository
public class TweetRepositoryImplementor extends SimpleJpaRepository<Tweet,Long> implements TweetRepository{
   
   private SessionFactory sessionFactory = HibernateBox.getSessionFactory();
   
   public TweetRepositoryImplementor(EntityManager entityManager){
      super(Tweet.class,entityManager);
   }
    
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
   
}
