package chessGamesDatabase.service;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OpeningService {
    Page<Opening> findAllPageable(Pageable pageable);

    Page<Opening> findByCodeContainingIgnoreCasePageable(String codeFilter, Pageable pageable);

    Page<Opening> findByNameContainingIgnoreCasePageable(String nameFilter, Pageable pageable);

    Page<Opening> findByPgnMovesContainingIgnoreCasePageable(String pgnMovesFilter, Pageable pageable);

    Page<Opening> findByCodeAndNameAndPgnMovesIgnoreCasePageable(String codeFilter, String nameFilter,
                                                                 String pgnMovesFilter, Pageable pageable);
    Opening findOpeningByPgn(String pgn);

}
