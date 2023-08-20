package chessGamesDatabase.service;

import chessGamesDatabase.dao.GameRepository;
import chessGamesDatabase.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Page<Game> findAllPageable(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }
}
