package com.cuda.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cuda.backend.entities.User;
import com.cuda.backend.entities.dto.UserCreationDTO;
import com.cuda.backend.entities.dto.UserDTO;
import com.cuda.backend.exceptions.RecordNotFoundException;
import com.cuda.backend.repository.UserRepository;

@Service
public class UserServiceImpl extends AbstractService implements UserService{
    
    @Autowired
    private UserRepository userRepository;

    public void register(UserCreationDTO userDto){
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setNickname(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setBio(userDto.getBio());
        userRepository.save(user);
    } 
    
    public void follow(Long followingId,Long followerId){
        userRepository.addFollower(followingId,followerId);
        userRepository.increaseFollowerCount(followingId);
        userRepository.increaseFollowingCount(followerId);
    }

    public void unfollow(Long followerId,Long followingId){
        userRepository.removeFollower(followerId,followingId);
        userRepository.decreaseFollowerCount(followingId);
        userRepository.decreaseFollowingCount(followerId);
    }
    
    public void login(String username,String password){
    }

    public void logout(Long id){
       Assert.notNull(id,"id cannot be null"); 
    }
    
    public User update(User user) {
    	return userRepository.save(user);
    }
    
    public void deleteById(Long userId){
        Assert.notNull(userId,"user id cannot be null");
        userRepository.deleteById(userId);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

    public UserDTO getById(Long id){
        Optional<UserDTO> userOpt = userRepository.getByIdDTO(id);
        if(userOpt.isEmpty()){
            throw new RecordNotFoundException("user not found");
        }else{
            return userOpt.get();
        }
    }
    
    public UserDTO getByName(String username){
        Optional<UserDTO> userOpt = userRepository.getByNameDTO(username);
        if(userOpt.isEmpty()){
            throw new RecordNotFoundException("user not found");
        }else{
            return userOpt.get();
        }
    }
    
    public List<UserDTO> getFollowers(Long id,int pageCount){
        return userRepository.getFollowers(id,pageCount,pageSize);
    }

    public List<UserDTO> getFollowing(Long id,int pageCount){
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

    public List<User> listOfActiveUsers(int pageNumber){
        User user = new User();
        user.setActive(true);
        
        Pageable pageRequest = PageRequest.of(pageNumber,pageSize);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<User> example = Example.of(user,exampleMatcher);
        Page<User> page = userRepository.findAll(example, pageRequest);

        return page.getContent();
    }

    public User getByIdExample(Long userId){
        User user = new User();
        user.setId(userId);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnorePaths("active","emailVerified");
        Example<User> example = Example.of(user,matcher);

        Optional<User> userOpt = userRepository.findOne(example);

        if(userOpt.isEmpty()){
            throw new RecordNotFoundException("user doesnt exists.");
        }else{
            return userOpt.get();
        }

    }

    public User getByNameExample(String username){
        User user = new User();
        user.setUsername(username);

        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnorePaths("active","emailVerified");
        Example<User> example = Example.of(user,matcher);

        Optional<User> userOpt = userRepository.findOne(example);

        if(userOpt.isEmpty()){
            throw new RecordNotFoundException("User not found.");
        }else{
            return userOpt.get();
        }
    }

    public boolean existsByName(String name){
        User user = new User();
        user.setUsername(name);

        Example<User> example = Example.of(user);

        Optional<User> userOptional =  userRepository.findOne(example);
        
        return userOptional.isPresent();
    }

    // public boolean existsByName(String username){
    //     return userRepository.existsByName(username);
    // }

    

    public List<User> getAll(){
        return userRepository.findAll();
    }
}
