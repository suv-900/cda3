package com.cuda.backend.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLikeRepositoryImpl implements CustomLikeRepository {
   
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void delete(Long tweetId,Long userId){
        String hql = "delete from Like where tweet.id = :tweetId and user.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();

        session.createMutationQuery(hql)
            .setParameter("tweetId",tweetId)
            .setParameter("userId",userId)
            .executeUpdate();

        tc.commit();
        session.close();
    }

    public boolean likeExists(Long tweetId,Long userId){
        String hql = "select 1 from Like l where l.tweet.id = :tweetId and l.user.id = :userId";
        Session session = sessionFactory.openSession();
        
        Integer result = session.createSelectionQuery(hql,Integer.class)
            .setParameter("tweetId",tweetId)
            .setParameter("userId",userId)
            .uniqueResult();
        session.close();
       
        return result == null ? false : true;
    }
}
