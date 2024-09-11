package com.cuda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cuda.backend.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>,CustomUserRepository{
}
