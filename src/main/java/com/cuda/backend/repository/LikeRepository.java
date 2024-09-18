package com.cuda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cuda.backend.entities.Like;

public interface LikeRepository extends JpaRepository<Like,Long>,CustomLikeRepository{
}
