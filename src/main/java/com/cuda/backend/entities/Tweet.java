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

    @NonNull
    @OneToOne(fetch = FetchType.EAGER)
    private User author;

    @Basic(fetch = FetchType.LAZY)
    private Set<String> imageLocation = new HashSet<>();

    private long likes = 0;

    @Basic(fetch = FetchType.LAZY)
    @OneToMany
    private Set<Tweet> replies = new HashSet<>();

    private LocalDateTime createdAt;

    @Basic(fetch = FetchType.LAZY)
    private LocalDateTime updatedAt;
    
    synchronized public void increaseLikes(){
        this.likes++;
    }

    synchronized public void decreaseLikes(){
        this.likes--;
    }
}

//two sessions holding the same object and tries to update counter;
