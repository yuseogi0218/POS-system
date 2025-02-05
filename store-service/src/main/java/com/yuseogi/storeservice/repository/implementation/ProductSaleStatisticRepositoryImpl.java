package com.yuseogi.storeservice.repository.implementation;

import com.yuseogi.storeservice.dto.response.GetProductSaleStatisticResponseDto;
import com.yuseogi.storeservice.repository.ProductSaleStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductSaleStatisticRepositoryImpl implements ProductSaleStatisticRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<GetProductSaleStatisticResponseDto.Product> findBy(Long userId, String category, String dateTerm, LocalDate startDate, String criteria) {
        String sql = String.format("""
            SELECT
                p.name          AS name,
                pss.sale_count  AS sale_count,
                pss.sale_amount AS sale_amount
            FROM product_sale_statistic pss
                JOIN product p ON pss.product_id = p.id
                JOIN store s ON p.store_id = s.id
            WHERE
                s.owner_user_id = ?
                AND p.category = ?
                AND pss.date_term = ?
                AND pss.start_date = ?
            ORDER BY pss.%s DESC, p.name ASC LIMIT 5
        """, criteria);

        RowMapper<GetProductSaleStatisticResponseDto.Product> rowMapper = (ResultSet rs, int rowNum) ->
            new GetProductSaleStatisticResponseDto.Product(
                rs.getString("name"),
                rs.getInt("sale_count"),
                rs.getInt("sale_amount")
            );

        return jdbcTemplate.query(sql, rowMapper, userId, category, dateTerm, startDate);
    }

}
