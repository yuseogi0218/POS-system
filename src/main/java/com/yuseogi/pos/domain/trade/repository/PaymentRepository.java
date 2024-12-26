package com.yuseogi.pos.domain.trade.repository;

import com.yuseogi.pos.domain.trade.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
