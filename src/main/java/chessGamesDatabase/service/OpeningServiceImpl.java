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
}
