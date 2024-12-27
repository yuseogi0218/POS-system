package com.yuseogi.pos.domain.trade.unit.repository;

import com.yuseogi.pos.common.RepositoryUnitTest;
import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDto;
import com.yuseogi.pos.domain.trade.dto.response.GetTradeIsNotCompletedResponseDtoBuilder;
import com.yuseogi.pos.domain.trade.entity.TradeEntity;
import com.yuseogi.pos.domain.trade.entity.TradeEntityBuilder;
import com.yuseogi.pos.domain.trade.repository.TradeRepository;
import com.yuseogi.pos.domain.trade.repository.dto.GetTradeIsNotCompletedDto;
import com.yuseogi.pos.domain.trade.repository.dto.mapper.GetTradeIsNotCompletedDtoMapper;
import com.yuseogi.pos.domain.trade.unit.repository.dto.GetTradeIsNotCompletedDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = TradeRepository.class)
public class TradeRepositoryUnitTest extends RepositoryUnitTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    void findFirstByTradeDeviceIdAndIsNotCompleted_존재_O() {
        // given
        Long tradeDeviceId = 1L;
        TradeEntity expectedTrade = TradeEntityBuilder.build();

        // when
        Optional<TradeEntity> optionalTrade = tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(tradeDeviceId);

        // then
        Assertions.assertThat(optionalTrade.isPresent()).isTrue();
        optionalTrade.ifPresent(
            actualTrade -> {
                Assertions.assertThat(actualTrade.getTradeDevice().getId()).isEqualTo(tradeDeviceId);
                TradeEntityBuilder.assertTrade(actualTrade, expectedTrade);
            }
        );
    }

    @Test
    void findFirstByTradeDeviceIdAndIsNotCompleted_존재_X() {
        // given
        Long unknownTradeDeviceId = 0L;

        // when
        Optional<TradeEntity> optionalTrade = tradeRepository.findFirstByTradeDeviceIdAndIsNotCompleted(unknownTradeDeviceId);

        // then
        Assertions.assertThat(optionalTrade.isEmpty()).isTrue();
    }

    @Test
    void existsByTradeDeviceIdAndIsNotCompleted_존재_O() {
        // given
        Long tradeDeviceId = 1L;

        // when
        boolean actualTradeIsExist = tradeRepository.existsByTradeDeviceIdAndIsNotCompleted(tradeDeviceId);

        // then
        Assertions.assertThat(actualTradeIsExist).isTrue();
    }

    @Test
    void existsByTradeDeviceIdAndIsNotCompleted_존재_X() {
        // given
        Long unknownTradeDeviceId = 0L;

        // when
        boolean actualTradeIsExist = tradeRepository.existsByTradeDeviceIdAndIsNotCompleted(unknownTradeDeviceId);

        // then
        Assertions.assertThat(actualTradeIsExist).isFalse();
    }

    @Test
    void findByTradeDeviceAndIsNotCompleted() {
        // given
        Long tradeDeviceId = 1L;
        List<GetTradeIsNotCompletedDto> expectedGetTradeIsNotCompletedDtoList = GetTradeIsNotCompletedDtoBuilder.buildList();
        GetTradeIsNotCompletedResponseDto expectedGetTradeIsNotCompletedResponse = GetTradeIsNotCompletedResponseDtoBuilder.build();

        // when
        List<GetTradeIsNotCompletedDto> actualGetTradeIsNotCompletedDtoList = tradeRepository.findByTradeDeviceAndIsNotCompleted(tradeDeviceId);
        GetTradeIsNotCompletedResponseDto actualGetTradeIsNotCompletedResponse = GetTradeIsNotCompletedDtoMapper.mapToResponseDto(actualGetTradeIsNotCompletedDtoList);

        // then
        Assertions.assertThat(actualGetTradeIsNotCompletedDtoList).isEqualTo(expectedGetTradeIsNotCompletedDtoList);
        Assertions.assertThat(actualGetTradeIsNotCompletedResponse).isEqualTo(expectedGetTradeIsNotCompletedResponse);
    }
}
