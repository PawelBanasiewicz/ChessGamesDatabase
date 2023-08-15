package chessGamesDatabase.service;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OpeningService {
    Page<Opening> findAllPageable(Pageable pageable);

}
