package com.cuda.backend.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

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
}
