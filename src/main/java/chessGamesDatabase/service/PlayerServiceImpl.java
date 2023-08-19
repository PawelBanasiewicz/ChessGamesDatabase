package chessGamesDatabase.service;

import chessGamesDatabase.dao.PlayerRepository;
import chessGamesDatabase.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<Player> findAllPageable(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Page<Player> findAllWithFiltersPageable(String firstName, String lastName, LocalDate birthDateFrom, LocalDate birthDateTo,
                                                   Character sex, Integer eloMin, Integer eloMax, Pageable pageable) {
        return playerRepository.findAllWithFilters(firstName, lastName, birthDateFrom, birthDateTo, sex, eloMin, eloMax, pageable);
    }
}
