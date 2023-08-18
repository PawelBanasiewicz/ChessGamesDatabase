package chessGamesDatabase.service;

import chessGamesDatabase.dao.OpeningRepository;
import chessGamesDatabase.entity.Opening;
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
    public Page<Opening> findByCodeContainingIgnoreCase(String codeFilter, Pageable pageable) {
        return openingRepository.findByCodeContainingIgnoreCase(codeFilter, pageable);
    }

    @Override
    public Page<Opening> findByNameContainingIgnoreCase(String nameFilter, Pageable pageable) {
        return openingRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
    }

    @Override
    public Page<Opening> findByPgnMovesContainingIgnoreCase(String pgnMovesFilter, Pageable pageable) {
        return openingRepository.findByPgnMovesContainingIgnoreCase(pgnMovesFilter, pageable);
    }
}
