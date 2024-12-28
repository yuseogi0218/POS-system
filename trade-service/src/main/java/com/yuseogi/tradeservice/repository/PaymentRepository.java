package com.yuseogi.tradeservice.repository;

import com.yuseogi.tradeservice.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
}
