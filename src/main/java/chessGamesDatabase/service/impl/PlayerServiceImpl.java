package chessGamesDatabase.service.impl;

import chessGamesDatabase.dao.PlayerRepository;
import chessGamesDatabase.entity.Player;
import chessGamesDatabase.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<Player> findAllPlayersWithFiltersPageable(String firstName, String lastName, LocalDate birthDateFrom, LocalDate birthDateTo,
                                                          Character sex, Integer eloMin, Integer eloMax, Pageable pageable) {
        return playerRepository.findAllPlayersWithFiltersPageable(firstName, lastName, birthDateFrom, birthDateTo, sex, eloMin, eloMax, pageable);
    }

    @Override
    public Player findPlayerById(Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        return player.orElse(null);
    }

    @Override
    public Player findPlayerByFirstNameAndLastName(String firstName, String lastName) {
        return playerRepository.findPlayerByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    @Transactional
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    @Transactional
    public void deletePlayerById(Long playerId) {
        playerRepository.deleteById(playerId);
    }

    @Override
    public Page<Player> findAllUsersFavoritePlayersWithFiltersPageable(Long userId, String firstNameFilter, String lastNameFilter,
                                                                       LocalDate birthDateFromFilter, LocalDate birthDateToFilter,
                                                                       Character sexFilter, Integer eloMinFilter,
                                                                       Integer eloMaxFilter, Pageable pageable) {
        return playerRepository.findAllUsersFavoritePlayersWithFiltersPageable(userId, firstNameFilter, lastNameFilter,
                birthDateFromFilter, birthDateToFilter, sexFilter, eloMinFilter, eloMaxFilter, pageable);
    }
}
