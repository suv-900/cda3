package com.cuda.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import com.cuda.backend.entities.User;
import com.cuda.backend.repository.UserRepository;

public class UserServiceImplementor extends AbstractService implements UserService{
    
    @Autowired
    private UserRepository userRepository;

    public Long register(String name,String password,String email){
        User user = new User();
        user.setName(name);
        user.setEmail(email); 
        String hashedPassword = ""; 
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
    
    public void login(String username,String password){

    }

    public void logout(Long id){
       Assert.notNull(id,"id cannot be null"); 
    }

    public void delete(Long id){

        Assert.notNull(id,"user id cannot be null");

        User user = new User();
        user.setId(id);
        userRepository.delete(user);
    }

    public Optional<User> getById(Long id){
        return userRepository.findById(id);
    }

    public List<User> getFollowers(Long id,int pageCount){
        return userRepository.getFollowers(id,pageCount,pageSize);
    }

    public List<User> getFollowing(Long id,int pageCount){
        return userRepository.getFollowing(id,pageCount,pageSize);
    }

    public List<User> findAll(int pageCount){

        Pageable pageRequest = PageRequest.of(pageCount,pageSize);
        Page<User> resultPage = userRepository.findAll(pageRequest);
         
        return resultPage.getContent();
    }

    public long count(){
        return userRepository.count();
    }

    public List<User> getActiveUsersList(int pageNumber){
        User user = new User();
        user.setActive(true);
        
        Pageable pageRequest = PageRequest.of(pageNumber,pageSize);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<User> example = Example.of(user,exampleMatcher);
        Page<User> page = userRepository.findAll(example, pageRequest);

        return page.getContent();
    }

    public boolean existsByName(String name){
        User user = new User();
        user.setName(name);

        Example<User> example = Example.of(user);

        Optional<User> userOptional =  userRepository.findOne(example);
        
        return userOptional.isPresent();
    }
}
