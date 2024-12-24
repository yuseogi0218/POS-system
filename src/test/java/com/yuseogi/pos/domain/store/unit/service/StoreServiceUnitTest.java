package com.yuseogi.pos.domain.store.unit.service;

import com.yuseogi.pos.common.ServiceUnitTest;
import com.yuseogi.pos.common.exception.CustomException;
import com.yuseogi.pos.domain.store.dto.request.CreateStoreRequestDto;
import com.yuseogi.pos.domain.store.entity.StoreEntity;
import com.yuseogi.pos.domain.store.exception.StoreErrorCode;
import com.yuseogi.pos.domain.store.repository.StoreRepository;
import com.yuseogi.pos.domain.store.service.TradeDeviceService;
import com.yuseogi.pos.domain.store.service.implementation.StoreServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class StoreServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private TradeDeviceService tradeDeviceService;

    /**
     * 상점 생성 성공
     */
    @Test
    void createStore_성공() {
        // given
        CreateStoreRequestDto request = mock(CreateStoreRequestDto.class);
        StoreEntity store = mock(StoreEntity.class);
        StoreEntity savedStore = mock(StoreEntity.class);

        // stub
        when(request.toStoreEntity()).thenReturn(store);
        when(storeRepository.save(store)).thenReturn(savedStore);

        // when
        storeService.createStore(request);

        // then
        verify(tradeDeviceService, never()).createTradeDevice(store);
        verify(tradeDeviceService, times(1)).createTradeDevice(savedStore);
    }

    /**
     * 상점 주인 사용자 이메일 기준으로 상점 조회 성공
     */
    @Test
    void getStore_by_ownerUserEmail_성공() {
        // given
        String ownerUserEmail = "user@domain.com";
        StoreEntity expectedStore = mock(StoreEntity.class);

        // stub
        when(storeRepository.findFirstByOwnerUserEmail(ownerUserEmail)).thenReturn(Optional.of(expectedStore));

        // when
        StoreEntity actualStore = storeService.getStore(ownerUserEmail);

        // then
        Assertions.assertThat(actualStore).isEqualTo(expectedStore);
    }

    /**
     * 상점 주인 사용자 이메일 기준으로 상점 조회 실패
     * - 실패 사유 : 존재하지 않는 상점
     */
    @Test
    void getStore_by_ownerUserEmail_실패_NOT_FOUND_STORE() {
        // given
        String unknownOwnerUserEmail = "unknown-user@domain.com";

        // stub
        when(storeRepository.findFirstByOwnerUserEmail(unknownOwnerUserEmail)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> storeService.getStore(unknownOwnerUserEmail))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_STORE.getMessage());
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 상점 조회 성공
     */
    @Test
    void getStore_by_tradeDeviceId_성공() {
        // given
        Long tradeDeviceId = 1L;
        StoreEntity expectedStore = mock(StoreEntity.class);

        // stub
        when(storeRepository.findFirstByTradeDeviceId(tradeDeviceId)).thenReturn(Optional.of(expectedStore));

        // when
        StoreEntity actualStore = storeService.getStore(tradeDeviceId);

        // then
        Assertions.assertThat(actualStore).isEqualTo(expectedStore);
    }

    /**
     * 주문용 태블릿 기기 DB Id 기준으로 상점 조회 실패
     * - 실패 사유 : 존재하지 않는 상점
     */
    @Test
    void getStore_by_tradeDeviceId_실패_NOT_FOUND_STORE() {
        // given
        Long unknownTradeDeviceId = 0L;

        // stub
        when(storeRepository.findFirstByTradeDeviceId(unknownTradeDeviceId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> storeService.getStore(unknownTradeDeviceId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_STORE.getMessage());
    }
}
