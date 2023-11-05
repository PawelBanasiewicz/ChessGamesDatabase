package chessGamesDatabase.service;

import chessGamesDatabase.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PlayerService {
    Page<Player> findAllPlayersWithFiltersPageable(String firstName, String lastName, LocalDate birthDateFrom,
                                                   LocalDate birthDateTo, Character sex, Integer eloMin,
                                                   Integer eloMax, Pageable pageable);

    Player findPlayerById(Long playerId);

    Player findPlayerByFirstNameAndLastName(String firstName, String lastName);

    void savePlayer(Player player);

    void deletePlayerById(Long playerId);

    Page<Player> findUsersFavoritePlayersWithFiltersPageable(Long userId, String firstNameFilter,
                                                             String lastNameFilter, LocalDate birthDateFromFilter,
                                                             LocalDate birthDateToFilter, Character sexFilter,
                                                             Integer eloMinFilter, Integer eloMaxFilter,
                                                             Pageable pageable);
}
