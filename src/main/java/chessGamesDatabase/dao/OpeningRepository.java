package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningRepository extends JpaRepository<Opening, Long> {
    Page<Opening> findByCodeContainingIgnoreCase(String code, Pageable pageable);

    Page<Opening> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Opening> findByPgnMovesContainingIgnoreCase(String pgnMoves, Pageable pageable);
}
