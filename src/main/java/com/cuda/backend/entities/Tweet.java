package com.cuda.backend.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tweet")
@Table(name = "tweets")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
   
    @NonNull
    private String tweet;

    private AtomicLong likeCount = new AtomicLong();

    private AtomicLong viewCount = new AtomicLong();

    @Transient
    private boolean likedByUser;

    @JsonIgnore
    @JoinTable(
        name = "tweet_likes",
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> userLikes = new ArrayList<>();

    @JsonIgnore
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id") //creates a forieng key column
    private User author;

    @JsonIgnore
    @ManyToOne
    @Basic(fetch = FetchType.LAZY)
    private Tweet parentTweet = null;

    @JsonIgnore
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "parentTweet",
    cascade = CascadeType.REMOVE, 
    fetch = FetchType.EAGER)
    private List<Tweet> replies = new ArrayList<>();

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name="updated_at")
    @Basic(fetch = FetchType.EAGER)
    private LocalDateTime updatedAt;
    
    public long increaseLikeCount(){
        return this.likeCount.incrementAndGet();
    }
    
    public long decreaseLikeCount(){
        return this.likeCount.decrementAndGet();
    }

    public long getLikeCount(){
        return this.likeCount.get();
    }

    public long increaseViewCount(){
        return this.viewCount.incrementAndGet();
    }

    public long decreaseViewCount(){
        return this.viewCount.decrementAndGet();
    }

    public long getViewCount(){
        return this.viewCount.get();
    }
    
    public void addUserToLikes(User user){
        this.userLikes.add(user);
    }

    public void removeUserFromLikes(User user){
        this.userLikes.remove(user);
    }
}

//two sessions holding the same object and tries to update counter;
//monitor(lock) for every object so that only one thread is accessing at a time. 