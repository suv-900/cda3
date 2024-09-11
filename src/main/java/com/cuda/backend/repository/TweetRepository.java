package com.cuda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cuda.backend.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long>,CustomTweetRepository{
}

