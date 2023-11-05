package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpeningRepository extends JpaRepository<Opening, Long> {
    @Query("SELECT o FROM Opening o " +
            "WHERE (:codeFilter IS NULL OR LENGTH(:codeFilter) = 0 OR o.code = :codeFilter) " +
            "AND (:nameFilter IS NULL OR o.name LIKE %:nameFilter%) " +
            "AND (:pgnMovesFilter IS NULL OR o.pgnMoves LIKE %:pgnMovesFilter%)"
    )
    Page<Opening> findOpeningsWithFiltersPageable(
            @Param("codeFilter") String codeFilter,
            @Param("nameFilter") String nameFilter,
            @Param("pgnMovesFilter") String pgnMovesFilter,
            Pageable pageable
    );

    @Query("SELECT o FROM Opening o WHERE :pgnMoves LIKE CONCAT('%', o.pgnMoves, '%') " +
            "ORDER BY LENGTH(o.pgnMoves) DESC " +
            "LIMIT 1")
    Opening findOpeningByPgn(@Param("pgnMoves") String pgnMovesFilter);

    @Query("SELECT o FROM Opening o WHERE o.openingId = :openingId")
    Opening findOpeningById(Long openingId);

    @Query("SELECT o FROM Opening o " +
            "JOIN o.followers u " +
            "WHERE u.userId = :userId " +
            "AND (:codeFilter IS NULL OR LENGTH(:codeFilter) = 0 OR o.code = :codeFilter) " +
            "AND (:nameFilter IS NULL OR o.name LIKE %:nameFilter%) " +
            "AND (:pgnMovesFilter IS NULL OR o.pgnMoves LIKE %:pgnMovesFilter%)"
    )
    Page<Opening> findUsersFavoriteOpeningsWithFiltersPageable(
            @Param("userId") Long userId,
            @Param("codeFilter") String codeFilter,
            @Param("nameFilter") String nameFilter,
            @Param("pgnMovesFilter") String pgnMovesFilter,
            Pageable pageable
    );

}