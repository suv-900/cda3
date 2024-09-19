package com.cuda.backend.web;

import com.cuda.backend.entities.User;
import com.cuda.backend.entities.dto.UserCreationDTO;
import com.cuda.backend.entities.dto.UserDTO;
import com.cuda.backend.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserHandler{

    @Autowired
    private UserService userService;


    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/getByName")
    public ModelAndView getByName(@NotNull @RequestParam String username){
        UserDTO user = userService.getByName(username);
        ModelAndView mv = new ModelAndView("user");
        mv.addObject("user",user);
        return mv;
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/getById")
    public ModelAndView getById(@NotNull @RequestParam Long userId){
        UserDTO user = userService.getById(userId);
        ModelAndView mv = new ModelAndView("user");
        mv.addObject("user",user);
        return mv;
    }

    @GetMapping(path = "/exists")
    public void exists(@RequestParam String username,HttpServletResponse response){
    	if(userService.existsByName(username)) {
    		response.setStatus(409);
    	}else {
    		response.setStatus(200);
    	}
    }
   
    @GetMapping("/register-page")
    public ModelAndView serveRegisterPage(){
        ModelAndView mv = new ModelAndView("register");
        mv.addObject("user",new UserCreationDTO());
        return mv;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void register(@Valid UserCreationDTO user){
        userService.register(user);
    }

    // @ResponseStatus(code = HttpStatus.OK)
    // @PostMapping(path = "/setNickname")
    // public void setNickName(@NotNull @RequestParam Long userId,@NotNull @RequestParam String nickName){
    //     userService.setNickName(userId,nickName);
    // }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/delete")
    public void delete(@NotNull @RequestParam Long id){
        userService.deleteById(id);
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
    public List<UserDTO> getFollowers(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return userService.getFollowers(userId,pageCount);
    }
    
    @GetMapping(path = "/getFollowing")
    public List<UserDTO> getFollowing(@NotNull @RequestParam Long userId,@NotNull @RequestParam int pageCount){
        return userService.getFollowing(userId,pageCount);
    }
    
}
