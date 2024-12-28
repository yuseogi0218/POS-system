package com.yuseogi.tradeservice.entity;

import com.yuseogi.tradeservice.entity.type.CardCompany;
import com.yuseogi.tradeservice.entity.type.PaymentMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @Builder(builderMethodName = "builderAsCashPay", buildMethodName = "buildAsCashPay")
    public PaymentEntity(TradeEntity trade) {
        this.trade = trade;
        this.method = PaymentMethod.CASH;
    }

    @Builder(builderMethodName = "builderAsCardPay", buildMethodName = "buildAsCardPay")
    public PaymentEntity(TradeEntity trade, CardCompany cardCompany) {
        this.trade = trade;
        this.method = PaymentMethod.CARD;
        this.cardCompany = cardCompany;
    }
}
