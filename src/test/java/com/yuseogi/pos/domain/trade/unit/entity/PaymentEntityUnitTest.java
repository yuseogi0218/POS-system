package com.yuseogi.pos.domain.trade.unit.entity;

import com.yuseogi.pos.domain.trade.entity.PaymentEntity;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import com.yuseogi.pos.domain.trade.entity.type.CardCompany;
import com.yuseogi.pos.domain.trade.entity.type.PaymentMethod;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class PaymentEntityUnitTest {

    @Test
    void constructorAsCashPay() {
        // given
        TradeEntity expectedTrade = mock(TradeEntity.class);
        PaymentMethod expectedMethod = PaymentMethod.CASH;

        // when
        PaymentEntity actualPayment = new PaymentEntity(expectedTrade);

        // then
        Assertions.assertThat(actualPayment.getTrade()).isEqualTo(expectedTrade);
        Assertions.assertThat(actualPayment.getMethod()).isEqualTo(expectedMethod);
        Assertions.assertThat(actualPayment.getCardCompany()).isNull();
    }

    @Test
    void constructorAsCardPay() {
        // given
        TradeEntity expectedTrade = mock(TradeEntity.class);
        PaymentMethod expectedMethod = PaymentMethod.CARD;
        CardCompany expectedCardCompany = CardCompany.K;

        // when
        PaymentEntity actualPayment = new PaymentEntity(expectedTrade, expectedCardCompany);

        // then
        Assertions.assertThat(actualPayment.getTrade()).isEqualTo(expectedTrade);
        Assertions.assertThat(actualPayment.getMethod()).isEqualTo(expectedMethod);
        Assertions.assertThat(actualPayment.getCardCompany()).isEqualTo(expectedCardCompany);
    }
}
