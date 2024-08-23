package com.cuda.backend.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.cuda.backend.entities.Tweet;
import com.cuda.backend.utilsbox.HibernateBox;

import jakarta.persistence.EntityNotFoundException;

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
    
}
