package com.yuseogi.pos.domain.trade.entity;

import com.yuseogi.pos.domain.trade.entity.type.CardCompany;
import com.yuseogi.pos.domain.trade.entity.type.PaymentMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id", nullable = false, updatable = false)
    private TradeEntity trade;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, updatable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_company", updatable = false)
    private CardCompany cardCompany;

    @CreatedDate
    @Column(name = "payment_at", nullable = false, updatable = false)
    private LocalDateTime paymentAt;
}
