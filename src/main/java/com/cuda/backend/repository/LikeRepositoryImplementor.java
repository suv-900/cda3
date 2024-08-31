package com.cuda.backend.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.cuda.backend.entities.Like;

import jakarta.persistence.EntityManager;

public class LikeRepositoryImplementor extends SimpleJpaRepository<Like,Long> implements LikeRepository{
    
    public LikeRepositoryImplementor(EntityManager entityManager){
        super(Like.class,entityManager);
    }

}
