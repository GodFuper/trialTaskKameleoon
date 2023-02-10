package com.example.trialtaskkameleoon.dao.repository;

import com.example.trialtaskkameleoon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}