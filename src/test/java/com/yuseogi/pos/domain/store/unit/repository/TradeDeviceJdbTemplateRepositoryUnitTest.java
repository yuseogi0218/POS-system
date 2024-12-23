package com.yuseogi.pos.domain.store.unit.repository;

import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.entity.TradeDeviceEntity;
import com.yuseogi.pos.domain.store.repository.TradeDeviceJdbcTemplateRepository;
import com.yuseogi.pos.domain.store.repository.implementation.TradeDeviceJdbcTemplateRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TradeDeviceJdbTemplateRepositoryUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TradeDeviceJdbcTemplateRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAll() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        TradeDeviceEntity tradeDevice1 = mock(TradeDeviceEntity.class);
        TradeDeviceEntity tradeDevice2 = mock(TradeDeviceEntity.class);
        List<TradeDeviceEntity> tradeDeviceList = List.of(tradeDevice1, tradeDevice2);

        // stub
        when(store.getId()).thenReturn(1L);
        when(tradeDevice1.getStore()).thenReturn(store);
        when(tradeDevice2.getStore()).thenReturn(store);

        // when
        repository.saveAll(tradeDeviceList);

        // then
        verify(jdbcTemplate, times(1)).batchUpdate(
            eq("INSERT INTO trade_device (store_id) VALUES (?)"),
            eq(tradeDeviceList),
            eq(tradeDeviceList.size()),
            any());
    }
}
