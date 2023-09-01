package chessGamesDatabase.service;

import chessGamesDatabase.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface GameService {

    Page<Game> findAllGamesPageable(Pageable pageable);

    Page<Game> findAllGamesWithFiltersPageable(String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                               String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                               Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                               Pageable pageable);

    Page<Game> findAllGamesPlayedByPlayer(Long playerId, Pageable pageable);

    Page<Game> findAllGamesPlayedByPlayerWithFilters(Long playerId, String openingCodeFilter, String player1FirstNameFilter, String player1LastNameFilter,
                                                     String player2FirstNameFilter, String player2LastNameFilter, String resultFilter,
                                                     Integer movesNumberMinFilter, Integer movesNumberMaxFilter, LocalDate dateFromFilter, LocalDate dateToFilter,
                                                     Pageable pageable);
}
