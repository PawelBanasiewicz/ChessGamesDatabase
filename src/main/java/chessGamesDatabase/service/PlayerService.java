package chessGamesDatabase.service;

import chessGamesDatabase.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PlayerService {
    Page<Player> findAllPageable(Pageable pageable);
    Page<Player> findAllWithFiltersPageable(String firstName, String lastName, LocalDate birthDateFrom, LocalDate birthDateTo,
                                            Character sex, Integer eloMin, Integer eloMax, Pageable pageable);
}