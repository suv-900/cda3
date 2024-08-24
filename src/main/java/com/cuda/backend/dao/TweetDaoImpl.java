package com.cuda.backend.dao;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;
import org.springframework.transaction.annotation.Transactional;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.utilsbox.HibernateBox;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;

public class TweetDaoImpl implements TweetDao{
    
    private SessionFactory sessionFactory = HibernateBox.getSessionFactory();

    private final int transaction_timeout = 5;
    private final int batch_size = 10;

    public void add(Tweet tweet)throws Exception{

        try(Session session = sessionFactory.openSession()){
            Transaction tc = session.beginTransaction();

            session.persist(tweet);

            session.flush();
            tc.commit();
        }catch(Exception e){
            throw e;
        }
    }

    @Transactional(timeout = transaction_timeout)
    public Optional<Tweet> view(long tweetID)throws Exception{
        Optional<Tweet> tweet = Optional.empty();

        try(Session session = sessionFactory.openSession()){

            tweet = session.byId(Tweet.class).loadOptional(tweetID);
        
            session.flush();
        }catch(Exception e){
            throw e;
        }

        return tweet;
    }

    @Transactional(timeout = transaction_timeout)
    public void update(Tweet tweet)throws Exception{
        try(Session session = sessionFactory.openSession()){
            Transaction tc = session.beginTransaction();

            session.merge(tweet);

            session.flush();
            tc.commit();
        }catch(Exception e){
            throw e;
        }
    }
    
    @Transactional(timeout = transaction_timeout)
    public void delete(Tweet tweet)throws Exception{
        try(Session session = sessionFactory.openSession()){
            Transaction tc = session.beginTransaction();
            
            session.remove(tweet);
            
            session.flush();
            tc.commit();
        }catch(Exception e){
            throw e;
        }
    }

    public void dislikeTweet(long tweetID)throws EntityNotFoundException,HibernateException,RollbackException,IllegalStateException{
        
        Transaction transaction = null;
        
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            transaction.setTimeout(transaction_timeout);

            Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetID);
            if(tweetOptional.isEmpty()){
                throw new EntityNotFoundException(); 
            }

            Tweet tweet = tweetOptional.get();
            tweet.decreaseLikes();
            
            session.merge(tweet);

            session.flush();
            transaction.commit();
        }catch(EntityNotFoundException e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }catch(HibernateException e){
            //given class doesnt resolve to a mapped entity
            if(transaction != null){
                transaction.rollback();
            } 
            throw e;
        }catch(RollbackException e){
            //commit failed
            if(transaction != null){
                transaction.rollback();
            } 
            throw e;
        }catch(IllegalStateException e){
            //transaction is dead 
            if(transaction != null){
                transaction.rollback();
            } 
            throw e;
        }
    }


    public void likeTweet(long tweetID)throws 
        EntityNotFoundException,HibernateException,RollbackException,IllegalStateException{
        
        Transaction transaction = null;
        
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            transaction.setTimeout(transaction_timeout);

            Optional<Tweet> tweetOptional = session.byId(Tweet.class).loadOptional(tweetID);
            if(tweetOptional.isEmpty()){
                throw new EntityNotFoundException(); 
            }

            Tweet tweet = tweetOptional.get();
            tweet.increaseLikes();
            
            session.merge(tweet);

            session.flush();
            transaction.commit();
        }catch(EntityNotFoundException e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }catch(HibernateException e){
            //given class doesnt resolve to a mapped entity
            if(transaction != null){
                transaction.rollback();
            } 
            throw e;
        }catch(RollbackException e){
            //commit failed
            if(transaction != null){
                transaction.rollback();
            } 
            throw e;
        }catch(IllegalStateException e){
            //transaction is dead 
            if(transaction != null){
                transaction.rollback();
            } 
            throw e;
        }
    
    }

    public List<Tweet> getByIds(List<Long> tweetIds)throws HibernateException,Exception,IllegalArgumentException{
        if(tweetIds.size() > 10){
            throw new IllegalArgumentException("lenghts of ids cannot be more than 10."); 
        }
        
        List<Tweet> tweetList = new LinkedList<>();
        try{
            Session session = sessionFactory.openSession();

            tweetList = session.byMultipleIds(Tweet.class)
                .enableOrderedReturn(false)//doesnt matter should improve perf
                .enableReturnOfDeletedEntities(false)//return instances marked for removal in current sesion but present in database
                .multiLoad(tweetIds);
            
            session.close();
        }catch(HibernateException e){
            //prolly occured during session.close()
            throw e;
        }catch(Exception e){
            throw e;
        }

        return tweetList;
    }

    public List<Tweet> getUserTweetsOldest(long userID)throws HibernateException,Exception{
        List<Tweet> tweetList = new LinkedList<>();

        Transaction transaction = null;
        try{
            String hql = "from Tweets t where t.user.id = ? order by created_at asc";
            
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            transaction.setTimeout(transaction_timeout);
            
            SelectionQuery<Tweet> query = session.createSelectionQuery(hql,Tweet.class);
            query.setParameter(0,userID);
            tweetList = query.getResultList();
        }catch(HibernateException e){
            if(transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }catch(Exception e){
            if(transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }

        return tweetList;
    }
}