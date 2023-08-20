package chessGamesDatabase.service;

import chessGamesDatabase.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameService {

    Page<Game> findAllPageable(Pageable pageable);
}
