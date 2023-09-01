package chessGamesDatabase.service;

import chessGamesDatabase.dao.GameRepository;
import chessGamesDatabase.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GameServiceImpl implements GameService {

    GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Page<Game> findAllGamesPageable(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Page<Game> findAllGamesWithFiltersPageable(String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                                      String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                                      Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                                      Pageable pageable) {
        return gameRepository.findAllGamesWithFilters(openingCodeFilter, player1FirstNameFilter, player1LastNameFilter, player2FirstNameFilter, player2LastNameFilter,
                resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageable);
    }

    @Override
    public Page<Game> findAllGamesPlayedByPlayer(Long playerId, Pageable pageable) {
        return gameRepository.findAllGamesPlayedByPlayer(playerId, pageable);
    }

}

