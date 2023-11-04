package chessGamesDatabase.service.impl;

import chessGamesDatabase.dao.GameRepository;
import chessGamesDatabase.entity.Game;
import chessGamesDatabase.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Page<Game> findAllGamesWithFiltersPageable(String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                                      String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                                      Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                                      Pageable pageable) {
        return gameRepository.findAllGamesWithFiltersPageable(openingCodeFilter, player1FirstNameFilter, player1LastNameFilter, player2FirstNameFilter, player2LastNameFilter,
                resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageable);
    }

    @Override
    public Page<Game> findAllGamesPlayedByPlayerWithFiltersPageable(Long playerId, String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                                                    String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                                                    Integer movesNumberMinFilter, Integer movesNumberMaxFilter,
                                                                    LocalDate dateFromFilter, LocalDate dateToFilter, Pageable pageable) {
        return gameRepository.findAllGamesPlayedByPlayerWithFiltersPageable(playerId, openingCodeFilter, player1FirstNameFilter, player1LastNameFilter,
                player2FirstNameFilter, player2LastNameFilter, resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageable);
    }

    @Override
    public Game findGameById(Long gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    @Override
    public List<Game> findAllGamesPlayedByPlayer(Long playerId) {
        return gameRepository.findAllGamesPlayedByPlayer(playerId);
    }

    @Override
    @Transactional
    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    @Override
    @Transactional
    public void deleteGameById(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    @Override
    public Page<Game> findAllUsersFavoriteGamesWithFiltersPageable(Long userId, String openingCodeFilter, String player1FirstNameFilter,
                                                                   String player1LastNameFilter, String player2FirstNameFilter, String player2LastNameFilter,
                                                                   String resultFilter, Integer movesNumberMinFilter, Integer movesNumberMaxFilter,
                                                                   LocalDate dateFromFilter, LocalDate dateToFilter, Pageable pageable) {
        return gameRepository.findAllUsersFavoriteGamesWithFiltersPageable(userId, openingCodeFilter,
                player1FirstNameFilter, player1LastNameFilter, player2FirstNameFilter, player2LastNameFilter,
                resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageable);
    }

    @Override
    public Page<Game> findAllGamesStartedWithOpeningWithFiltersPageable(Long openingId, String player1FirstNameFilter, String player1LastNameFilter,
                                                                        String player2FirstNameFilter, String player2LastNameFilter,
                                                                        String resultFilter, Integer movesNumberMinFilter, Integer movesNumberMaxFilter,
                                                                        LocalDate dateFromFilter, LocalDate dateToFilter, Pageable pageable) {
        return gameRepository.findAllGamesStartedWithOpeningWithFiltersPageable(openingId, player1FirstNameFilter, player1LastNameFilter, player2FirstNameFilter,
                player2LastNameFilter, resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageable);
    }
}

