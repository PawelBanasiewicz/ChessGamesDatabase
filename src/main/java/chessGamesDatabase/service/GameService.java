package chessGamesDatabase.service;

import chessGamesDatabase.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GameService {
    Page<Game> findAllGamesWithFiltersPageable(String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                               String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                               Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                               Pageable pageable);

    Page<Game> findAllGamesPlayedByPlayerWithFiltersPageable(Long playerId, String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                                             String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                                             Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                                             Pageable pageable);

    Game findGameById(Long gameId);

    List<Game> findAllGamesPlayedByPlayer(Long playerId);

    void saveGame(Game game);

    void deleteGameById(Long gameId);

    Page<Game> findAllUsersFavoriteGamesWithFiltersPageable(Long userId, String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                                            String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                                            Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                                            Pageable pageable);

    Page<Game> findAllGamesStartedWithOpeningWithFiltersPageable(Long openingId, String player1FirstNameFilter, String player1LastNameFilter, String player2FirstNameFilter,
                                                                 String player2LastNameFilter, String resultFilter, Integer movesNumberMinFilter, Integer movesNumberMaxFilter,
                                                                 LocalDate dateFromFilter, LocalDate dateToFilter, Pageable pageable);
}
