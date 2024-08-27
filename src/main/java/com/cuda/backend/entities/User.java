package com.cuda.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyGroup;
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
import jakarta.persistence.OneToMany;
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
    @LazyGroup("lazy_email_group") //remember bytecode enhancement
    @Basic(fetch = FetchType.LAZY)
    private String email;

    @LazyGroup("lazy_email_group")
    @Basic(fetch = FetchType.LAZY)
    private boolean emailVerified;

    @NonNull
    @Basic(fetch=FetchType.LAZY)
    private String password;
   
    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> following = new LinkedList<>();

    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> followers = new LinkedList<>();


    @BatchSize(size = 10)
    @OneToMany(fetch = FetchType.LAZY)
    private List<Tweet> tweets = new LinkedList<>();

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
