package com.moongcheap_backend.demand.infrastructure.demand;

import com.moongcheap_backend.demand.domain.demand.DemandStatus;
import com.moongcheap_backend.demand.presentation.demand.dto.DemandListDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DemandQueryRepositoryImpl implements DemandQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<DemandListDto.DemandItemDto> MAPPER = (rs, rowNum) -> {
        Long boardId = rs.getObject("board_id", Long.class);
        DemandListDto.DemandBoardDto board =
            boardId == null ? null : new DemandListDto.DemandBoardDto(
                boardId,
                rs.getInt("participant_count"),
                rs.getObject("board_price_min", Integer.class),
                rs.getObject("board_price_max", Integer.class),
                rs.getObject("board_sale_end_at", LocalDateTime.class)
            );
        return new DemandListDto.DemandItemDto(
            rs.getLong("id"),
            DemandStatus.valueOf(rs.getString("status")),
            rs.getObject("desired_price_min", Integer.class),
            rs.getObject("desired_price_max", Integer.class),
            rs.getObject("desire_end_at", LocalDateTime.class),
            rs.getObject("quantity", Integer.class),
            rs.getString("extra_requirement"),
            rs.getBoolean("is_substitutable"),
            new DemandListDto.CatalogDto(
                rs.getLong("catalog_id"),
                rs.getString("catalog_name"),
                rs.getString("catalog_thumbnail_url"),
                rs.getObject("catalog_list_price", Integer.class)
            ),
            board
        );
    };

    private static final String BY_ID_QUERY = """
        SELECT
            d.id,
            d.status,
            d.desired_price_min,
            d.desired_price_max,
            d.desire_end_at,
            d.quantity,
            d.extra_requirement,
            d.is_substitutable,
            pc.id            AS catalog_id,
            pc.name          AS catalog_name,
            pc.thumbnail_url AS catalog_thumbnail_url,
            pc.list_price    AS catalog_list_price,
            db.id            AS board_id,
            db.participant_count,
            db.price_min     AS board_price_min,
            db.price_max     AS board_price_max,
            db.sale_end_at   AS board_sale_end_at
        FROM demand d
        INNER JOIN product_catalog pc ON d.catalog_id = pc.id
        LEFT  JOIN demand_board   db ON d.demand_board_id = db.id
        WHERE d.id = :demandId
          AND d.member_id = :memberId
        """;

    private static final String QUERY_TEMPLATE = """
        SELECT
            d.id,
            d.status,
            d.desired_price_min,
            d.desired_price_max,
            d.desire_end_at,
            d.quantity,
            d.extra_requirement,
            d.is_substitutable,
            pc.id            AS catalog_id,
            pc.name          AS catalog_name,
            pc.thumbnail_url AS catalog_thumbnail_url,
            pc.list_price AS catalog_list_price,
            db.id            AS board_id,
            db.participant_count,
            db.price_min     AS board_price_min,
            db.price_max     AS board_price_max,
            db.sale_end_at   AS board_sale_end_at
        FROM (
            SELECT *
            FROM demand
            WHERE member_id = :memberId
              AND status IN (%s)
            ORDER BY created_at DESC, id DESC
            LIMIT :limit OFFSET :offset
        ) d
        INNER JOIN product_catalog pc ON d.catalog_id = pc.id
        LEFT  JOIN demand_board   db ON d.demand_board_id = db.id
        """;

    @Override
    public Optional<DemandListDto.DemandItemDto> findDemandItemByIdAndMemberId(Long demandId,
        Long memberId) {
        return jdbcTemplate.query(
            BY_ID_QUERY,
            Map.of("demandId", demandId, "memberId", memberId),
            MAPPER
        ).stream().findFirst();
    }

    @Override
    public List<DemandListDto.DemandItemDto> findDemandItemsByMemberId(Long memberId,
        List<DemandStatus> statuses, Pageable pageable) {
        String inClause = statuses.stream()
            .map(s -> "'" + s.name() + "'")
            .collect(Collectors.joining(", "));
        return jdbcTemplate.query(
            String.format(QUERY_TEMPLATE, inClause),
            Map.of("memberId", memberId, "limit", pageable.getPageSize(), "offset",
                pageable.getOffset()),
            MAPPER
        );
    }
}
