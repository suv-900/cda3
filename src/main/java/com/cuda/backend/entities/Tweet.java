package com.cuda.backend.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   
    
    @NonNull
    private String tweet;

    private long likes = 0;

    private long viewCount = 0;

    private Category category = Category.UNLISTED;

    @NonNull
    @OneToOne(fetch = FetchType.EAGER)
    private User author;

    @Basic(fetch = FetchType.LAZY)
    private Set<String> imageLocation = new HashSet<>();

    @Basic(fetch = FetchType.LAZY)
    @OneToMany
    private Set<Tweet> replies = new HashSet<>();

    private LocalDateTime createdAt;

    @Basic(fetch = FetchType.LAZY)
    private LocalDateTime updatedAt;
    
    public synchronized void increaseLikes(){
        //lock acquire
        this.likes++;
        //lock release
    }

    public synchronized void decreaseLikes(){
        this.likes--;
    }

    public synchronized long getLikes(){
        return this.likes;
    }

    public synchronized void increaseViewCount(){
        this.viewCount++;
    }

    public synchronized void decreaseViewCount(){
        this.viewCount--;
    }

    public synchronized long getViewCount(){
        return this.viewCount;
    }
}

//two sessions holding the same object and tries to update counter;
//monitor(lock) for every object so that only one thread is accessing at a time. 