package com.yuseogi.userservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "phone", length = 11)
    private String phone;

    @Builder
    public UserEntity(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

}
