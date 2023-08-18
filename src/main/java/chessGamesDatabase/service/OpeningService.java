package chessGamesDatabase.service;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OpeningService {
    Page<Opening> findAllPageable(Pageable pageable);

    Page<Opening> findByCodeContainingIgnoreCase(String codeFilter, Pageable pageable);

    Page<Opening> findByNameContainingIgnoreCase(String nameFilter, Pageable pageable);

    Page<Opening> findByPgnMovesContainingIgnoreCase(String pgnMovesFilter, Pageable pageable);

}
