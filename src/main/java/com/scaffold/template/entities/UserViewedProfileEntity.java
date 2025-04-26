package com.scaffold.template.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_viewed_profiles")
@Data
public class UserViewedProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "viewer_id", nullable = false)
    private UserEntity viewer;

    @ManyToOne
    @JoinColumn(name = "viewed_user_id", nullable = false)
    private UserEntity viewedUser;

    private LocalDateTime viewedAt;

    @Enumerated(EnumType.STRING)
    private ViewStatus status; // PENDING, ACCEPTED, REJECTED

    private Boolean isMatch; // Si terminó en un match real
}