package com.example.trialtaskkameleoon.dao.repository;

import com.example.trialtaskkameleoon.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByCookie(String cookie);
}