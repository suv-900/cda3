package com.cuda.backend.dao;

import java.util.Optional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.utilsbox.HibernateBox;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;

public class TweetDaoImpl implements TweetDao{
    
    private SessionFactory sessionFactory = HibernateBox.getSessionFactory();

    private final int transaction_timeout = 5;

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

    public void dislikeTweet(long tweetID)throws EntityNotFoundException,HibernateException{
        
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


    public void likeTweet(long tweetID)throws EntityNotFoundException,HibernateException{
        
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
}
