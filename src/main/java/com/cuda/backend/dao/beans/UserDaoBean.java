package com.cuda.backend.dao.beans;

import java.util.Optional;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cuda.backend.dao.contracts.UserDao;
import com.cuda.backend.entities.User;
import com.cuda.backend.utilsbox.HibernateBox;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;

//servlet
//acceptor threads -> request queue -> worker threads

/*
 server.tomcat.accept-count=100 # Maximum queue length for incoming connection requests when all possible request processing threads are in use.
server.tomcat.max-connections=10000 # Maximum number of connections that the server accepts and processes at any given time.
server.tomcat.max-threads=200 # Maximum amount of worker threads.
server.tomcat.min-spare-threads=10 # Minimum amount of worker threads.
 */

//using methods outside session or persistence context leads to
//LazyInitialisationException

//low level uses transactions or caches to get objects.
//session for every request model
//waste of 1 cache due to openning new sessions
@Repository
public class UserDaoBean implements UserDao{
    private final int transaction_timeout = 10;

    private SessionFactory sessionFactory = HibernateBox.getSessionFactory();

    @Transactional(timeout = transaction_timeout)
    public void add(User user)throws Exception{
        try(Session session = sessionFactory.openSession()){
            Transaction tc = session.beginTransaction();
            
            session.persist(user);
            
            session.flush();
            tc.commit();
        }catch(Exception e){
            throw e;
        }
    }

    // public void addUser(User user)throws RuntimeException{
    //     Transaction tc = null;

    //     //session.close() frees up connection
    //     try(Session session = sessionFactory.openSession()){
    //         tc = session.beginTransaction();
    //         tc.setTimeout(transaction_timeout);
            
    //         //unit of work
    //         session.persist(user);

    //         //session.flush()generated sql statements.syncs state of 
    //         //persistent context and db state
    //         session.flush(); 
    //         tc.commit();
    //     }catch(RuntimeException e){
    //         if(tc != null) tc.rollback();
    //         // if(tc == null) throw new ErrorCreatingTransaction(e.getMessage());
             
    //         throw e;  
    //     }
    // }

    public Optional<User> getByName(String username)throws Exception{
        Optional<User> user = Optional.empty();
        
        try(Session session = sessionFactory.openSession()){
            
            user = session.byNaturalId(User.class)
            .using("username",username).loadOptional();

            session.flush();
        }catch(Exception e){
            throw e;
        }

        return user;
    }

    public Optional<User> getByIdOptional(long userID)throws Exception{
        Optional<User> user= Optional.empty();
        
        try{
            Session session = sessionFactory.getCurrentSession();

            user = session.byId(User.class).loadOptional(userID);

        }catch(Exception e){
            throw e;
        }

        return user; 
    }

    public User getById(Long userID)throws Exception{
        User user = null;
        
        try{
            Session session = sessionFactory.getCurrentSession();

            user = session.byId(User.class).load(userID);

        }catch(Exception e){
            throw e;
        }

        return user;
    }
    //proxy objects save a lot of memory
    //either gets from 1 cache or from db
    // @Transactional(timeout=transaction_timeout)
    // public Optional<User> get(Long userID)throws Exception{
    //     Optional<User> user = Optional.empty();
    //     try(Session session = sessionFactory.openSession()){
    //         user = session.byId(User.class).loadOptional(userID);
    //     }catch(Exception e){
    //         throw e;
    //     }

    //     return user;
    // }
    
    

    //writing sql only when necessary
    //one session per request model
    
    
    
    
    /**
     * checks for duplicate entity in persistence context and in persistence store
     * @param user
     * @return
     * @throws RuntimeException
     */
    @Transactional(timeout = 5)
     public boolean exists(User user)throws RuntimeException{
        boolean inPersistentContext = false;
        boolean inPersistentStore = false;
        
        try(Session session = sessionFactory.openSession()){


            inPersistentContext = session.contains(user);
            
            if(!inPersistentContext){
                User duplicateUser = session.byNaturalId(User.class)
                .using("username",user.getName()).load();
            
                if(duplicateUser == null){
                    inPersistentStore = false;
                }else{
                    inPersistentStore = true;
                }    
            }

        }catch(RuntimeException e){
            throw e;
        }
        return inPersistentContext || inPersistentStore;
    }
    
    public String getUserPassword(String username)throws EntityNotFoundException{
        String dbpassword = null;

        try(Session session = sessionFactory.openSession()){
            // Optional<User> user = session.byNaturalId(User.class)
            //     .using("username",username)
            //     .loadOptional();
            // if(user.isEmpty()){
            //     throw new RecordNotFoundException();
            // }
            User user = session.byNaturalId(User.class)
                .using("username",username)
                .getReference();
            //no find()

            dbpassword = user.getPassword();

            // String hql = "select password from User where username = :username";
            // dbpassword = session.createSelectionQuery(hql,String.class)
            //     .setParameter("username",username)
            //     .getSingleResult();
        }catch(EntityNotFoundException e){
            //thrown if record doesnt exists after using entity functions -> getReference()
            throw e;
        }
        
        return dbpassword;
    }
    /**
     * checks if user exists by given username
     * @param username
     * @return
     * @throws Exception
     */
    public boolean exists(Example<User> user)throws Exception{
        boolean userExists = false;

        try{
            Session session = sessionFactory.getCurrentSession();
            
            session.contains(user)
        } 

        return userExists;
    }


    @Transactional(timeout = 5)
    public User update(User user)throws Exception{
        User updatedUser = null;
        try(Session session = sessionFactory.openSession()){
            Transaction tc = session.beginTransaction();

            updatedUser = session.merge(user);

            session.flush();
            tc.commit();
        }catch(Exception e){
            throw e;
        }

        return updatedUser;
    }    


    public void delete(User user)throws Exception,HibernateException,IllegalStateException,RollbackException{
        Transaction transaction = null;
        try{
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            transaction.setTimeout(transaction_timeout);

            session.remove(user);

            session.flush();
            transaction.commit();
            session.close();
        }catch(HibernateException e){
            if(transaction != null && transaction.isActive()) transaction.rollback(); 
            throw e;
        }catch(RollbackException e){
            if(transaction != null && transaction.isActive()) transaction.rollback(); 
            throw e;
        }catch(IllegalStateException e){
            if(transaction != null && transaction.isActive()) transaction.rollback(); 
            throw e;
        }
    }
    //dont call close on getCurrentSession();

    //get user in batches
    //this function doesnt care about owner info just wants the users following the owner
    public Set<User> getUserFollowers(Long userID)throws EntityNotFoundException,Exception{
        Set<User> followers = null;
        
        try(Session session = sessionFactory.openSession()){
            //referencing is a way to check if user exists 
            User user = session.byId(User.class).getReference(userID);
            followers = user.getFollowers();
        }catch(EntityNotFoundException e){
            throw e;
        }catch(Exception err){
            throw err;
        }

        return followers;    
    }

    //use batches
    public Set<User> getUserFollowing(Long userID)throws EntityNotFoundException,Exception{
        Set<User> following = null;
        
        try(Session session = sessionFactory.openSession()){
            User user = session.byId(User.class).getReference(userID);
            following = user.getFollowing();

        }catch(EntityNotFoundException e){
            throw e;
        }catch(Exception err){
            throw err;
        }

        return following;    
    }
    
    
    public void addFollower(Long ownerID,Long followerID){

        try(Session session = sessionFactory.openSession()){
            User owner = session.find(User.class,ownerID);
            User follower = session.find(User.class,followerID);

            owner.addFollower(follower);
            follower.addFollowing(owner);

            Transaction tc = session.beginTransaction();
            
            session.merge(owner);
            session.merge(follower);

            session.flush();

            tc.commit();
        }catch(Exception e){

        }
    }


    /**
     * follower function.also checks the existence.merges
     * @param owner
     * @param follower
     * @throws EntityNotFoundException
     */
    @Transactional(timeout = 5)
    public void addFollower2(User owner,User follower)throws EntityNotFoundException{

        //2 trip could be 1 using just update ids
        try(Session session = sessionFactory.openSession()){

            //or get blank entity and merge
            User user1 = session.getReference(owner);
            //imagine 2mil followers
            //now batched
            user1.getFollowers().add(follower);

            session.merge(user1);

            User user2 = session.getReference(follower);
            user2.getFollowing().add(owner);
            session.merge(user2);

            session.flush();
        }catch(EntityNotFoundException e){
           throw e; 
        }
    }

    //merges 2 new objects with existing objects in the database
    @Transactional(timeout = 5)
    public void addFollower(User owner,User follower)throws Exception{
        try(Session session = sessionFactory.openSession()){
            
            //2 update statements
            //batch update to 1 request
            owner.addFollower(follower);
            owner = session.merge(owner);

            follower.addFollowing(owner);
            follower = session.merge(follower);

        }catch(Exception e){
            throw e;
        }

    }

    @Transactional(timeout = 5)
    public void removeFollower(User owner,User follower)throws EntityNotFoundException{
        try(Session session = sessionFactory.openSession()){
            User user1 = session.byId(User.class).getReference(owner.getId());
            user1.removeFollower(follower);

            session.merge(user1);

            User user2 = session.byId(User.class).getReference(follower.getId());
            follower.addFollowing(owner);

            session.merge(user2);

            session.flush();

        }catch(EntityNotFoundException e){
            throw e;
        }
    }
    // @Transactional(timeout = 5)
    // public void removeFollower(User owner,User follower)throws Exception{
    //     try(Session session = sessionFactory.openSession()){
    //         owner.removeFollower
    //         owner = session.merge(owner);

    //         follower.getFollowing().remove(owner);
    //         follower = session.merge(follower);
    //     }catch(Exception e){
    //         throw e;
    //     }

    // }
}
