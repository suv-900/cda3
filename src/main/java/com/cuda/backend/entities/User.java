package com.cuda.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@NaturalIdCache
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;//allow illegal objects with null id
   
    @NonNull
    @NaturalId
    private String name;
    
    private String nickname;

    private boolean active;
    
    @NonNull
    @Basic(fetch = FetchType.LAZY)
    private String email;

    @NonNull
    @Basic(fetch=FetchType.LAZY)
    private String password;
   
    
    @BatchSize(size = 10)
    @ManyToMany
    @Basic(optional=false,fetch=FetchType.LAZY)
    private Set<User> following = new HashSet<>();

    @BatchSize(size = 10)
    @ManyToMany
    @Basic(optional=false,fetch=FetchType.LAZY)
    private Set<User> followers = new HashSet<>();

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @Basic(fetch=FetchType.LAZY)
    private LocalDateTime updatedAt;

    public void addFollower(User follower){
        this.followers.add(follower);
    }

    public void addFollowing(User following){
        this.following.add(following);
    }

    public void removeFollower(User follower){
        this.followers.remove(follower);
    }

    public void removeFollowing(User following){
        this.following.remove(following);
    }
}
