package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.cuda.backend.entities.User;
import com.cuda.backend.entities.dto.UserDTO;
import com.cuda.backend.exceptions.RecordNotFoundException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {
    
    @Autowired
    private SessionFactory sessionFactory;

    // public Optional<User> existsByName(String username){
    //     Assert.notNull(username,"username cannot be null");
        
    //     Session session = sessionFactory.openSession();
    //     Optional<User> user = session.byNaturalId(User.class)
    //         .using("username",username).loadOptional();

    //     session.close();
    //     return user;
    // }

    public boolean existsByName(String username){
        String hql = "select 1 from User u where u.username = :username";
        Session session = sessionFactory.openSession();
        Integer result = session.createSelectionQuery(hql,Integer.class)
            .setParameter("username",username)
            .uniqueResult();
        session.close();
        return result == null?false:true;
    }
    
    public Optional<UserDTO> getByIdDTO(Long userId){
        String hql = """
                select new UserDTO(u.id,u.username,u.nickname,u.active,u.bio,u.followerCount,u.followingCount,u.tweetCount) 
                from User u where u.id = :userId";
        """;
        Session session = sessionFactory.openSession();
        Optional<UserDTO> user = session.createSelectionQuery(hql,UserDTO.class)
            .setParameter("userId",userId)
            .uniqueResultOptional();
        session.close();
        return user;

    
    }
    
    public Optional<UserDTO> getByNameDTO(String username){
        String hql = """
                select new UserDTO(u.id,u.username,u.nickname,u.active,u.bio,u.followerCount,u.followingCount,u.tweetCount) 
                from User u where u.username = :username
                """;
        Session session = sessionFactory.openSession();
        Optional<UserDTO> user = session.createSelectionQuery(hql,UserDTO.class)
            .setParameter("username",username)
            .uniqueResultOptional();
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
 
    public List<UserDTO> getFollowers(Long userId,int pageCount,int pageSize){
        Assert.notNull(userId,"id cannot be null");
        Assert.notNull(pageCount,"page offset cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        Assert.isTrue(pageCount < 50, "page count must not exceed 50");

        String hql = """
        select new UserDTO(u.id,u.username,u.nickname,u.active,u.bio
        ,u.followerCount,u.followingCount,u.tweetCount) from User u 
        where :user in elements(u.following)
        """;
 
        User user = new User();
        user.setId(userId);

        Session session = sessionFactory.openSession();
        Query<UserDTO> query = session.createQuery(hql,UserDTO.class);

        query.setParameter("user",user);
        query.setFirstResult(pageCount * pageSize);
        query.setMaxResults(pageSize);

        List<UserDTO> users = query.getResultList();
        session.close();

        return users;
    }

    public List<UserDTO> getFollowing(Long userId,int pageCount,int pageSize){
        Assert.notNull(userId,"id cannot be null");
        Assert.notNull(pageCount,"page offset cannot be null");
        Assert.notNull(pageSize,"page size cannot be null");
        Assert.isTrue(pageCount < 50, "page count must not exceed 100");

        String hql = """
        select new UserDTO(u.id,u.username,u.nickname,u.active,u.bio
        ,u.followerCount,u.followingCount,u.tweetCount) from User u 
        where :user in elements(u.followers)
        """;
       
        User user = new User();
        user.setId(userId);

        Session session = sessionFactory.openSession();
        Query<UserDTO> query = session.createQuery(hql,UserDTO.class);

        query.setParameter("user",user);
        query.setFirstResult(pageCount * pageSize);
        query.setMaxResults(pageSize);

        List<UserDTO> users = query.getResultList();
        session.close();

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
    
    @Transactional
    public void increaseFollowerCount(Long userId){
        String hql = "update User u set u.followerCount = u.followerCount+1 where u.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("userId",userId)
            .executeUpdate();
        session.close();
        tc.commit();
    }

    @Transactional
    public void increaseFollowingCount(Long userId){
        String hql = "update User u set u.followingCount = u.followingCount+1 where u.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("userId",userId)
            .executeUpdate();
        tc.commit();
        session.close();
    }
    @Transactional
    public void increaseTweetCount(Long userId){
        String hql = "update User u set u.tweetCount = u.tweetCount+1 where u.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("userId",userId)
            .executeUpdate();
        session.close();
        tc.commit();
    }

    @Transactional
    public void decreaseFollowerCount(Long userId){
        String hql = "update User u set u.followerCount = u.followerCount-1 where u.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("userId",userId)
            .executeUpdate();
        session.close();
        tc.commit();
    }

    @Transactional
    public void decreaseFollowingCount(Long userId){
        String hql = "update User u set u.followingCount = u.followingCount-1 where u.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("userId",userId)
            .executeUpdate();
        session.close();
        tc.commit();
    }
    @Transactional
    public void decreaseTweetCount(Long userId){
        String hql = "update User u set u.tweetCount = u.tweetCount-1 where u.id = :userId";
        Session session = sessionFactory.openSession();
        Transaction tc = session.beginTransaction();
        session.createMutationQuery(hql)
            .setParameter("userId",userId)
            .executeUpdate();
        session.close();
        tc.commit();
    }
}
