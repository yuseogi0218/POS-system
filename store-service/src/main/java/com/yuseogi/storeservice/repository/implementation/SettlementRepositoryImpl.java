package com.yuseogi.storeservice.repository.implementation;

import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class SettlementRepositoryImpl implements SettlementRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public GetSettlementResponseDto getSettlement(Long userId, String dateTerm, LocalDate startDate) {
        String sql = """
            SELECT
                s.revenue          AS revenue,
                s.fee              AS fee,
                s.operating_profit AS operating_profit
            FROM settlement s
                JOIN store st ON st.id = s.store_id
            WHERE
                st.owner_user_id = ?
                AND s.date_term = ?
                AND s.start_date = ?
            LIMIT 1;
        """;

        RowMapper<GetSettlementResponseDto> rowMapper = (ResultSet rs, int rowNum) ->
            new GetSettlementResponseDto(
                rs.getInt("revenue"),
                rs.getInt("fee"),
                rs.getInt("operating_profit")
            );

        List<GetSettlementResponseDto> settlementList = jdbcTemplate.query(sql, rowMapper, userId, dateTerm, startDate);

        return settlementList.isEmpty() ? null : settlementList.getFirst();
    }

}
