package com.yuseogi.pos.domain.store.entity;

import com.yuseogi.pos.domain.store.entity.type.PosGrade;
import com.yuseogi.pos.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerUserId")
    private UserEntity ownerUser;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "posGrade", nullable = false)
    private PosGrade posGrade;
}
