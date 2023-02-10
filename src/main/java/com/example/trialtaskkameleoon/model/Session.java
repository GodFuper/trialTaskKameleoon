package com.example.trialtaskkameleoon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "session", indexes = {
        @Index(name = "idx_session_cookie", columnList = "cookie")
})
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "cookie", unique = true)
    private String cookie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Session(User user) {
        this.user = user;
        this.cookie = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Session session = (Session) o;
        return id != null && Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}