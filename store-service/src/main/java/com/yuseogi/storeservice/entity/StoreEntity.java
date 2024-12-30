package com.yuseogi.storeservice.entity;

import com.yuseogi.storeservice.entity.type.PosGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "owner_user_id", nullable = false, updatable = false)
    private Long ownerUserId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "pos_grade", nullable = false)
    private PosGrade posGrade;

    @Builder
    public StoreEntity(Long ownerUserId, String name, PosGrade posGrade) {
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.posGrade = posGrade;
    }
}
