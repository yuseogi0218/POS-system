package com.yuseogi.storeservice.repository.implementation;

import com.yuseogi.storeservice.dto.response.GetSettlementDetailResponseDto;
import com.yuseogi.storeservice.dto.response.GetSettlementResponseDto;
import com.yuseogi.storeservice.repository.SettlementRepository;
import com.yuseogi.storeservice.repository.mapper.GetSettlementDetailRevenueProductCategoryMapper;
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

    @Override
    public GetSettlementDetailResponseDto.Revenue getSettlementDetailRevenue(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT
                p.category           AS category,
                p.name               AS name,
                SUM(od.count)        AS count,
                SUM(od.total_amount) AS amount
            FROM order_detail od
                JOIN product p ON p.id = od.product_id
                JOIN order_table ot ON ot.id = od.order_id
                JOIN trade t ON t.id = ot.trade_id
                JOIN payment pa ON pa.trade_id = t.id
                JOIN store s ON s.id = t.store_id
            WHERE
                s.owner_user_id = ?
                AND ? <= DATE(pa.created_at) AND DATE(pa.created_at) < ?
            GROUP BY p.id
        """;

        RowMapper<GetSettlementDetailResponseDto.Revenue.ProductCategory.Product> rowMapper = (ResultSet rs, int rowNum) ->
            new GetSettlementDetailResponseDto.Revenue.ProductCategory.Product(
                rs.getString("name"),
                rs.getString("category"),
                rs.getInt("count"),
                rs.getInt("amount")
            );

        List<GetSettlementDetailResponseDto.Revenue.ProductCategory.Product> productList = jdbcTemplate.query(sql, rowMapper, userId, startDate, endDate);

        List<GetSettlementDetailResponseDto.Revenue.ProductCategory> categoryList = GetSettlementDetailRevenueProductCategoryMapper.mapFromProductList(productList);

        return new GetSettlementDetailResponseDto.Revenue(categoryList);
    }

    @Override
    public GetSettlementDetailResponseDto.Fee getSettlementDetailFee(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT
                p.card_company  AS name,
                SUM(p.card_fee) AS amount
            FROM payment p
                JOIN trade t ON t.id = p.trade_id
                JOIN store s ON s.id = t.store_id
            WHERE
                s.owner_user_id = ?
                AND p.card_company IS NOT NULL
                AND ? <= DATE(p.created_at) AND DATE(p.created_at) < ?
            GROUP BY p.card_company;
        """;

        RowMapper<GetSettlementDetailResponseDto.Fee.CardCompany> rowMapper = (ResultSet rs, int rowNum) ->
            new GetSettlementDetailResponseDto.Fee.CardCompany(
                rs.getString("name"),
                rs.getInt("amount")
            );

        List<GetSettlementDetailResponseDto.Fee.CardCompany> cardCompanyList = jdbcTemplate.query(sql, rowMapper, userId, startDate, endDate);

        return new GetSettlementDetailResponseDto.Fee(cardCompanyList);
    }
}
