package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpeningRepository extends JpaRepository<Opening, Long> {
    Page<Opening> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    Page<Opening> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Opening> findByPgnMovesContainingIgnoreCase(String pgnMoves, Pageable pageable);

    Page<Opening> findByCodeContainingIgnoreCaseAndNameContainingIgnoreCaseAndPgnMovesContainingIgnoreCase
            (String code, String name, String pgnMoves, Pageable pageable);

    @Query("SELECT o FROM Opening o WHERE :pgnMoves LIKE CONCAT('%', o.pgnMoves, '%') " +
            "ORDER BY LENGTH(o.pgnMoves) DESC " +
            "LIMIT 1")
    Opening findOpeningByPgn(@Param("pgnMoves") String pgnMoves);
}
