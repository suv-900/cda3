package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {
    
    @Autowired
    private SessionFactory sessionFactory;

    public void init(){
        System.out.println("creating user_repository bean");
    }

    public void destroy(){
        System.out.println("destroying user_repository bean");
    }

    public Optional<User> existsByName(String username){
        
        Assert.notNull(username,"username cannot be null");
        
        Session session = sessionFactory.getCurrentSession();
        Optional<User> user = session.byNaturalId(User.class)
            .using("username",username).loadOptional();

        return user;
    }

    
    public String getUserPassword(String username){
        
        Assert.notNull(username,"username cannot be null");
        
        Session session = sessionFactory.getCurrentSession();
        String hashedPassword = session.byNaturalId(User.class)
            .using("username",username)
            .load()
            .getPassword();
        
        return hashedPassword;
    }

    public List<User> getFollowers(Long userID){

        Assert.notNull(userID,"user id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        User user = session.find(User.class,userID);
        
        return user.getFollowers();
    }
    
    public List<User> getFollowers(Long id,int pageNumber,int pageSize){
        Assert.notNull(id,"id cannot be null");
        Assert.notNull(pageNumber,"page offset cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        
        String sqlString = "select u.id,u.name from users u join relationships r on u.id = r.follower_id where r.following_id = ?";
        Session session = sessionFactory.openSession();
        Query<User> query = session.createNativeQuery(sqlString,User.class);
        
        query.setFirstResult(pageNumber);
        query.setFetchSize(pageSize);
        query.setParameter(0,id);

        return query.getResultList();
    }
    public List<User> getFollowing(Long id,int pageNumber,int pageSize){
        Assert.notNull(id,"id cannot be null");
        Assert.notNull(pageNumber,"page offset cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");

        String sqlString = "select u.id,u.name from users u join relationships r on u.id = r.following_id where r.follower_id = ?";
        Session session = sessionFactory.openSession();
        Query<User> query = session.createNativeQuery(sqlString,User.class);
        
        query.setFirstResult(pageNumber);
        query.setFetchSize(pageSize);
        query.setParameter(0,id);

        return query.getResultList();
    }
    public List<User> getFollowing(Long userID){

        Assert.notNull(userID,"user id cannot be null");

        Session session = sessionFactory.getCurrentSession();
        User user = session.find(User.class,userID);
        
        return user.getFollowing();
    }
   
    @Transactional
    public void addFollower(Long ownerID,Long followerID){

        Assert.notNull(ownerID,"owner id cannot be null");
        Assert.notNull(followerID,"follower id cannot be null");

        Session session = sessionFactory.openSession();
        Optional<User> ownerBox = session.byId(User.class).loadOptional(ownerID);
        if(ownerBox.isEmpty()){
            throw new RecordNotFoundException("owner not found");
        }
        Optional<User> followerBox = session.byId(User.class).loadOptional(followerID);
        if(followerBox.isEmpty()){
            throw new RecordNotFoundException("follower not found");
        }

        User owner = ownerBox.get();
        User follower = followerBox.get();

        owner.addFollower(follower);
        follower.addFollowing(owner);

        session.merge(owner);
        session.merge(follower);

    }
    
    @Transactional
    public void removeFollower(Long ownerID,Long followerID){

        Assert.notNull(ownerID,"owner id cannot be null");
        Assert.notNull(followerID,"follower id cannot be null");

        Session session = sessionFactory.openSession();
        Optional<User> ownerBox = session.byId(User.class).loadOptional(ownerID);
        if(ownerBox.isEmpty()){
            throw new EntityNotFoundException("owner entity not found");
        }

        Optional<User> followerBox = session.byId(User.class).loadOptional(followerID);
        if(followerBox.isEmpty()){
            throw new EntityNotFoundException("follower entity not found");
        }

        User owner = ownerBox.get();
        User follower = followerBox.get();

        owner.removeFollower(follower);
        follower.removeFollowing(owner);

        session.merge(owner);
        session.merge(follower);

    }
    
}
