package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cuda.backend.entities.User;
import com.cuda.backend.utilsbox.HibernateBox;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Repository
public class UserRepositoryImplementor extends SimpleJpaRepository<User,Long> implements UserRepository {
    
    private SessionFactory sessionFactory = HibernateBox.getSessionFactory();

    public UserRepositoryImplementor(EntityManager entityManager){
        super(User.class,entityManager);
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

        Session session = sessionFactory.getCurrentSession();
        Optional<User> ownerBox = session.byId(User.class).loadOptional(ownerID);
        if(ownerBox.isEmpty()){
            throw new EntityNotFoundException();
        }
        Optional<User> followerBox = session.byId(User.class).loadOptional(followerID);
        if(followerBox.isEmpty()){
            throw new EntityNotFoundException();
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

        Session session = sessionFactory.getCurrentSession();
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
