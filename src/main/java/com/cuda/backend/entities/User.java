package com.cuda.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NaturalIdCache
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;//allow illegal objects with null id
   
    @NotBlank(message = "username cannot be blank")
    @NaturalId
    private String username;
    
    private String nickname;

    private boolean active = false;
    
    @NotBlank(message = "email cannot be blank")
    @LazyGroup("lazy_email_group") //remember bytecode enhancement
    @Basic(fetch = FetchType.LAZY)
    private String email;

    @LazyGroup("lazy_email_group")
    @Basic(fetch = FetchType.LAZY)
    private boolean emailVerified = false;

    @Getter
    @Setter
    @NotBlank(message = "password cannot be blank")
    @Basic(fetch=FetchType.LAZY)
    private String password;
    
    @JoinTable(
        name = "relationships",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id") 
    )
    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> following = new ArrayList<>();

    
    @BatchSize(size = 10)
    @ManyToMany(mappedBy = "following",fetch = FetchType.LAZY)
    private List<User> followers = new ArrayList<>();


    @OneToMany(
        mappedBy = "user",
        targetEntity = Tweet.class,
        cascade = CascadeType.REMOVE, 
        fetch = FetchType.LAZY
    )
    @BatchSize(size = 10)
    private List<Tweet> tweets = new ArrayList<>();

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @Basic(fetch=FetchType.LAZY)
    private LocalDateTime updatedAt;

    public synchronized void addFollower(User follower){
        this.followers.add(follower);
    }

    public synchronized void addFollowing(User following){
        this.following.add(following);
    }

    public synchronized void removeFollower(User follower){
        this.followers.remove(follower);
    }

    public synchronized void removeFollowing(User following){
        this.following.remove(following);
    }
}
