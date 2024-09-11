package com.cuda.backend.web;

import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;
import com.cuda.backend.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserHandler{

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String returnHello(){
        return "Hello";
    }

    @GetMapping("/params/{id}")
    public String getParams(@PathVariable String id){
        return id;
    }

    @GetMapping(path = "/exists/{username}")
    public void exists(@PathVariable String username,HttpServletResponse response){
    	
    	if(userService.existsByName(username)) {
    		response.setStatus(409);
    	}else {
    		response.setStatus(200);
    	}
       
    }

    @PostMapping(path= "/login/{username}/{password}")
    public void login(@PathVariable String username,@PathVariable String password){

    } 

    @PostMapping(path = "/generate_token")
    @ResponseStatus(code = HttpStatus.OK)
    public String generateToken(@PathVariable String username,@PathVariable String password){
        return "";
    }

    @PostMapping(path = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void register(@Valid @RequestBody User user,
                         HttpServletResponse response){
        Long userId = userService.register(user);

        String token = "";

        response.setHeader("Authorization",token);
    }

    // @PutMapping(path = "/update")
    // public User update(@RequestBody User user){
    // 	return userService
    // }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/delete/{userId}")
    public void delete(@PathVariable Long userId){
        userService.delete(userId);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/get/{id}")
    public User getById(@PathVariable Long id){
        Optional<User> user = userService.getById(id);

        if(user.isEmpty()){
            throw new RecordNotFoundException("user not found");
        }else{
            return user.get();
        }

    }

    @PostMapping(path = "/followerUser/{followingId}")
    public void follow(@PathVariable Long followingId){
        Long followerId = 0L;
        userService.follow(followerId,followingId);
    }

    @PostMapping(path = "/unfollowUser/{followingId}")
    public void unfollow(@PathVariable Long followingId){
        Long followerId = 0L;
        userService.unfollow(followerId,followingId);
    }

    @GetMapping(path = "/getFollowers/{userId}/{pageCount}")
    public List<User> getFollowers(@PathVariable Long userId,@PathVariable int pageCount){
        return userService.getFollowers(userId,pageCount);
    }

    @GetMapping(path = "/getFollowing/{userId}/{pageCount}")
    public List<User> getFollowing(@PathVariable Long userId,@PathVariable int pageCount){
        return userService.getFollowing(userId,pageCount);
    }
}
