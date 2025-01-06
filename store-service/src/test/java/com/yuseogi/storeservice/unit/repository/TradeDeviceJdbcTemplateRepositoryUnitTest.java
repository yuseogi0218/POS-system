package com.yuseogi.storeservice.unit.repository;

import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.entity.TradeDeviceEntity;
import com.yuseogi.storeservice.repository.implementation.TradeDeviceJdbcTemplateRepositoryImpl;
import org.junit.jupiter.api.BeforeAll;
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

public class TradeDeviceJdbcTemplateRepositoryUnitTest extends JdbcTemplateRepositoryUnitTest {

    private TradeDeviceJdbcTemplateRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        this.repository = new TradeDeviceJdbcTemplateRepositoryImpl(jdbcTemplate);
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
    }
}
