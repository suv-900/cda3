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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    private AtomicLong likes = new AtomicLong();

    private AtomicLong viewCount = new AtomicLong();

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
    
    public long increaseLikes(){
        return this.likes.incrementAndGet();
    }
    
    public long decreaseLikes(){
        return this.likes.decrementAndGet();
    }

    public long getLikes(){
        return this.likes.get();
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
    
}

//two sessions holding the same object and tries to update counter;
//monitor(lock) for every object so that only one thread is accessing at a time. 