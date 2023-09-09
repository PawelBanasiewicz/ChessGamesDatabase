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
    public Page<Opening> findAllPageable(Pageable pageable) {
        return openingRepository.findAll(pageable);
    }

    @Override
    public Page<Opening> findByCodeContainingIgnoreCasePageable(String codeFilter, Pageable pageable) {
        return openingRepository.findByCodeContainingIgnoreCase(codeFilter, pageable);
    }

    @Override
    public Page<Opening> findByNameContainingIgnoreCasePageable(String nameFilter, Pageable pageable) {
        return openingRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
    }

    @Override
    public Page<Opening> findByPgnMovesContainingIgnoreCasePageable(String pgnMovesFilter, Pageable pageable) {
        return openingRepository.findByPgnMovesContainingIgnoreCase(pgnMovesFilter, pageable);
    }

    @Override
    public Page<Opening> findByCodeAndNameAndPgnMovesIgnoreCasePageable(String codeFilter, String nameFilter, String pgnMovesFilter, Pageable pageable) {
        return openingRepository.findByCodeContainingIgnoreCaseAndNameContainingIgnoreCaseAndPgnMovesContainingIgnoreCase
                (codeFilter, nameFilter, pgnMovesFilter, pageable);
    }
}
