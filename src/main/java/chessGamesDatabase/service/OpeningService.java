package chessGamesDatabase.service;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OpeningService {
    Page<Opening> findAllOpeningsWithFiltersPageable(String codeFilter, String nameFilter,
                                                     String pgnMovesFilter, Pageable pageable);
    Opening findOpeningByPgn(String pgn);

    Opening findOpeningById(Long openingId);
}
