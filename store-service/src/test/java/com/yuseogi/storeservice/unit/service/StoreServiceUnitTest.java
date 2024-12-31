package com.yuseogi.storeservice.unit.service;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.storeservice.dto.request.CreateStoreRequestDto;
import com.yuseogi.storeservice.entity.StoreEntity;
import com.yuseogi.storeservice.exception.StoreErrorCode;
import com.yuseogi.storeservice.repository.StoreRepository;
import com.yuseogi.storeservice.service.TradeDeviceService;
import com.yuseogi.storeservice.service.implementation.StoreServiceImpl;
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
     * 상점 DB Id 기준으로 상점 조회 성공
     */
    @Test
    void getStore_성공() {
        // given
        Long storeId = 1L;
        StoreEntity expectedStore = mock(StoreEntity.class);

        // stub
        when(storeRepository.findFirstById(storeId)).thenReturn(Optional.of(expectedStore));

        // when
        StoreEntity actualStore = storeService.getStore(storeId);

        // then
        Assertions.assertThat(actualStore).isEqualTo(expectedStore);
    }

    /**
     * 상점 DB Id 기준으로 상점 조회 실패
     * - 실패 사유 : 존재하지 않는 상점
     */
    @Test
    void getStore_실패_NOT_FOUND_STORE() {
        // given
        Long unknownStoreId = 0L;

        // stub
        when(storeRepository.findFirstById(unknownStoreId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> storeService.getStore(unknownStoreId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_STORE.getMessage());
    }

    /**
     * 상점 주인 사용자 DB Id 기준으로 상점 조회 성공
     */
    @Test
    void getStoreByOwnerUser_성공() {
        // given
        Long ownerUserId = 1L;
        StoreEntity expectedStore = mock(StoreEntity.class);

        // stub
        when(storeRepository.findFirstByOwnerUser(ownerUserId)).thenReturn(Optional.of(expectedStore));

        // when
        StoreEntity actualStore = storeService.getStoreByOwnerUser(ownerUserId);

        // then
        Assertions.assertThat(actualStore).isEqualTo(expectedStore);
    }

    /**
     * 상점 주인 사용자 DB Id 기준으로 상점 조회 실패
     * - 실패 사유 : 존재하지 않는 상점
     */
    @Test
    void getStoreByOwnerUser_실패_NOT_FOUND_STORE() {
        // given
        Long unknownOwnerUserId = 0L;

        // stub
        when(storeRepository.findFirstByOwnerUser(unknownOwnerUserId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> storeService.getStoreByOwnerUser(unknownOwnerUserId))
            .isInstanceOf(CustomException.class)
            .hasMessage(StoreErrorCode.NOT_FOUND_STORE.getMessage());
    }

}
