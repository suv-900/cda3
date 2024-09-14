package com.cuda.backend.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {
    
    @Autowired
    private SessionFactory sessionFactory;

    public Optional<User> existsByName(String username){
        Assert.notNull(username,"username cannot be null");
        
        Session session = sessionFactory.openSession();
        Optional<User> user = session.byNaturalId(User.class)
            .using("username",username).loadOptional();

        session.close();
        return user;
    }

    
    public String getUserPassword(String username){
        Assert.notNull(username,"username cannot be null");
        
        Session session = sessionFactory.openSession();
        Optional<User> userOpt = session.byNaturalId(User.class)
            .using("username",username)
            .loadOptional();
        session.close();
      
        if(userOpt.isEmpty()){
            throw new RecordNotFoundException();
        }

        return userOpt.get().getPassword();
    }
 
    public List<User> getFollowers(Long userId,int pageCount,int pageSize){
        Assert.notNull(userId,"id cannot be null");
        Assert.notNull(pageCount,"page offset cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        Assert.isTrue(pageCount < 50, "page count must not exceed 50");

        User followingUser = new User();
        followingUser.setId(userId);

        String hql = "select user.id,user.username,user.nickname from User as user where :followingUser in elements(user.following)";
        Session session = sessionFactory.openSession();
        Query<Tuple> query = session.createQuery(hql,Tuple.class);

        query.setParameter("followingUser",followingUser);
        query.setFirstResult(pageCount * pageSize);
        query.setMaxResults(pageSize);

        List<Tuple> result = query.getResultList();
        session.close();

        List<User> users = new LinkedList<>();
       
        //5ms
        for(Tuple tuple : result){
            User user = new User();
            user.setId((Long)tuple.get(0));
            user.setUsername((String)tuple.get(1));
            user.setNickname((String)tuple.get(2));

            users.add(user);
        }
        
        return users;
    }

    public List<User> getFollowing(Long userId,int pageCount,int pageSize){
        Assert.notNull(userId,"id cannot be null");
        Assert.notNull(pageCount,"page offset cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        Assert.isTrue(pageCount < 50, "page count must not exceed 100");


        User followerUser = new User();
        followerUser.setId(userId);

        String hql = "select u.id,u.username,u.nickname from User u where :followerUser in elemets(u.followers) order by u.followers";
        Session session = sessionFactory.openSession();
        Query<Tuple> query = session.createQuery(hql,Tuple.class);

        query.setParameter("followerUser",followerUser);
        query.setFirstResult(pageCount * pageSize);
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

    
  
    @Transactional
    public void addFollower(Long followingId,Long followerId){

        Assert.notNull(followingId,"following id cannot be null");
        Assert.notNull(followerId,"follower id cannot be null");

        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
    
        Optional<User> followingOpt = session.byId(User.class).loadOptional(followingId);
        if(followingOpt.isEmpty()){
            throw new RecordNotFoundException("following user not found");
        }
        Optional<User> followerOpt = session.byId(User.class).loadOptional(followerId);
        if(followerOpt.isEmpty()){
            throw new RecordNotFoundException("follower user not found");
        }

        User followingUser = followingOpt.get();
        User followerUser = followerOpt.get();

        followingUser.addFollower(followerUser);
        followerUser.addFollowing(followingUser);
        session.merge(followingUser);
        session.merge(followerUser);
        
        session.flush();
        tc.commit();

        session.close();
    }

    @Transactional
    public void removeFollower(Long followingId,Long followerId){

        Assert.notNull(followingId,"following id cannot be null");
        Assert.notNull(followerId,"follower id cannot be null");

        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
    
        Optional<User> followingOpt = session.byId(User.class).loadOptional(followingId);
        if(followingOpt.isEmpty()){
            throw new RecordNotFoundException("following user not found");
        }
        Optional<User> followerOpt = session.byId(User.class).loadOptional(followerId);
        if(followerOpt.isEmpty()){
            throw new RecordNotFoundException("follower user not found");
        }

        User followingUser = followingOpt.get();
        User followerUser = followerOpt.get();

        followingUser.removeFollower(followerUser);
        followerUser.removeFollowing(followingUser);
        session.merge(followingUser);
        session.merge(followerUser);
        
        session.flush();
        tc.commit();

        session.close();
    }
    
}
