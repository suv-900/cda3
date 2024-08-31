package com.cuda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cuda.backend.entities.Like;
import com.cuda.backend.entities.User;

public interface LikeRepository extends JpaRepository<Like,Long>{
    
}
