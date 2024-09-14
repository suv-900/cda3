package com.cuda.backend.web;

import com.cuda.backend.entities.User;
import com.cuda.backend.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserHandler{

    @Autowired
    private UserService userService;

    @GetMapping(path = "/exists")
    public void exists(@RequestParam String username,HttpServletResponse response){
    	if(userService.existsByName(username)) {
    		response.setStatus(409);
    	}else {
    		response.setStatus(200);
    	}
    }
    
    @PostMapping(path = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long register(@NotNull @RequestParam String username,
            @NotNull @RequestParam String email,
            @NotNull @RequestParam String password){
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return userService.register(user);
    }
    
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/delete")
    public void delete(@NotNull @RequestParam Long id){
        userService.deleteById(id);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/getById")
    public User getById(@NotNull @RequestParam Long id){
        return userService.getByIdExample(id);    
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/getByName")
    public User getByName(@NotNull @RequestParam String username){
        return userService.getByNameExample(username);
    }

    @GetMapping(path = "/getAll")
    public List<User> getAllUsers(){
        return userService.getAll();
    }
    
    @PostMapping(path = "/followUser")
    public void follow(@NotNull @RequestParam Long followingId,@NotNull @RequestParam Long followerId){
        userService.follow(followingId,followerId);
    }
    
    @PostMapping(path = "/unfollowUser")
    public void unfollow(@NotNull @RequestParam Long followingId,@NotNull @RequestParam Long followerId){
        userService.unfollow(followerId,followingId);
    }
   
    @GetMapping(path = "/getFollowers")
    public List<User> getFollowers(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return userService.getFollowers(userId,pageCount);
    }
    
    @GetMapping(path = "/getFollowing")
    public List<User> getFollowing(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return userService.getFollowing(userId,pageCount);
    }
    
}
