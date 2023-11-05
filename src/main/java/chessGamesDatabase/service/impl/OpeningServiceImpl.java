package chessGamesDatabase.service.impl;

import chessGamesDatabase.dao.OpeningRepository;
import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.service.OpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OpeningServiceImpl implements OpeningService {
    private final OpeningRepository openingRepository;

    @Autowired
    public OpeningServiceImpl(OpeningRepository openingRepository) {
        this.openingRepository = openingRepository;
    }

    @Override
    public Page<Opening> findAllOpeningsWithFiltersPageable(String codeFilter, String nameFilter,
                                                            String pgnMovesFilter, Pageable pageable) {
        return openingRepository.findOpeningsWithFiltersPageable(codeFilter, nameFilter, pgnMovesFilter, pageable);
    }

    @Override
    public Opening findOpeningByPgn(String pgn) {
        return openingRepository.findOpeningByPgn(pgn);
    }

    @Override
    public Opening findOpeningById(Long openingId) {
        return openingRepository.findOpeningById(openingId);
    }

    @Override
    public Page<Opening> findUsersFavoriteOpeningsWithFiltersPageable(Long userId, String codeFilter, String nameFilter,
                                                                      String pgnMovesFilter, Pageable pageable) {
        return openingRepository.findUsersFavoriteOpeningsWithFiltersPageable(
                userId, codeFilter, nameFilter, pgnMovesFilter, pageable
        );
    }
}
