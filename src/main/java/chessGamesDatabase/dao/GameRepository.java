package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g " +
            "LEFT JOIN g.player1 p1 " +
            "LEFT JOIN g.player2 p2 " +
            "JOIN g.opening o " +
            "WHERE (:openingIdFilter IS NULL OR LENGTH(:openingIdFilter) = 0 OR o.code = :openingIdFilter) " +
            "AND (:player1FirstNameFilter IS NULL OR p1 IS NULL OR p1.firstName LIKE %:player1FirstNameFilter%) " +
            "AND (:player1LastNameFilter IS NULL OR p1 IS NULL OR p1.lastName LIKE %:player1LastNameFilter%) " +
            "AND (:player2FirstNameFilter IS NULL OR p2 IS NULL OR p2.firstName LIKE %:player2FirstNameFilter%) " +
            "AND (:player2LastNameFilter IS NULL OR p2 IS NULL OR p2.lastName LIKE %:player2LastNameFilter%) " +
            "AND (:resultFilter IS NULL OR LENGTH(:resultFilter) = 0 OR g.result = :resultFilter) " +
            "AND (:movesNumberMinFilter IS NULL OR g.movesNumber >= :movesNumberMinFilter) " +
            "AND (:movesNumberMaxFilter IS NULL OR g.movesNumber <= :movesNumberMaxFilter) " +
            "AND (:dateFromFilter IS NULL OR g.date >= :dateFromFilter) " +
            "AND (:dateToFilter IS NULL OR g.date <= :dateToFilter)"
    )
    Page<Game> findAllGamesWithFiltersPageable(@Param("openingIdFilter") String openingIdFilter,
                                               @Param("player1FirstNameFilter") String player1FirstNameFilter,
                                               @Param("player1LastNameFilter") String player1LastNameFilter,
                                               @Param("player2FirstNameFilter") String player2FirstNameFilter,
                                               @Param("player2LastNameFilter") String player2LastNameFilter,
                                               @Param("resultFilter") String resultFilter,
                                               @Param("movesNumberMinFilter") Integer movesNumberMinFilter,
                                               @Param("movesNumberMaxFilter") Integer movesNumberMaxFilter,
                                               @Param("dateFromFilter") LocalDate dateFromFilter,
                                               @Param("dateToFilter") LocalDate dateToFilter,
                                               Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.player1.playerId = :playerId OR g.player2.playerId = :playerId")
    Page<Game> findAllGamesPlayedByPlayerPageable(@Param("playerId") Long playerId, Pageable pageable);

    @Query("SELECT g from Game g WHERE g.player1.playerId = :playerId OR g.player2.playerId = :playerId")
    List<Game> findAllGamesPlayedByPlayer(@Param("playerId") Long playerId);

    @Query("SELECT g FROM Game g " +
            "LEFT JOIN g.player1 p1 " +
            "LEFT JOIN g.player2 p2 " +
            "WHERE (p1.playerId = :playerId OR p2.playerId = :playerId) " +
            "AND (:openingCodeFilter IS NULL OR LENGTH(:openingCodeFilter) = 0 OR g.opening.code = :openingCodeFilter) " +
            "AND (:player1FirstNameFilter IS NULL OR p1 IS NULL OR g.player1.firstName LIKE %:player1FirstNameFilter%) " +
            "AND (:player1LastNameFilter IS NULL OR p1 IS NULL OR g.player1.lastName LIKE %:player1LastNameFilter%) " +
            "AND (:player2FirstNameFilter IS NULL OR p2 IS NULL OR g.player2.firstName LIKE %:player2FirstNameFilter%) " +
            "AND (:player2LastNameFilter IS NULL OR p2 IS NULL OR g.player2.lastName LIKE %:player2LastNameFilter%) " +
            "AND (:resultFilter IS NULL OR LENGTH(:resultFilter) = 0 OR g.result = :resultFilter) " +
            "AND (:movesNumberMinFilter IS NULL OR g.movesNumber >= :movesNumberMinFilter) " +
            "AND (:movesNumberMaxFilter IS NULL OR g.movesNumber <= :movesNumberMaxFilter) " +
            "AND (:dateFromFilter IS NULL OR g.date >= :dateFromFilter) " +
            "AND (:dateToFilter IS NULL OR g.date <= :dateToFilter)"
    )
    Page<Game> findAllGamesPlayedByPlayerWithFiltersPageable(@Param("playerId") Long playerId,
                                                             @Param("openingCodeFilter") String openingCodeFilter,
                                                             @Param("player1FirstNameFilter") String player1FirstNameFilter,
                                                             @Param("player1LastNameFilter") String player1LastNameFilter,
                                                             @Param("player2FirstNameFilter") String player2FirstNameFilter,
                                                             @Param("player2LastNameFilter") String player2LastNameFilter,
                                                             @Param("resultFilter") String resultFilter,
                                                             @Param("movesNumberMinFilter") Integer movesNumberMinFilter,
                                                             @Param("movesNumberMaxFilter") Integer movesNumberMaxFilter,
                                                             @Param("dateFromFilter") LocalDate dateFromFilter,
                                                             @Param("dateToFilter") LocalDate dateToFilter,
                                                             Pageable pageable);
    @Query("SELECT g FROM Game g " +
            "JOIN g.followers u " +
            "LEFT JOIN g.player1 p1 " +
            "LEFT JOIN g.player2 p2 " +
            "WHERE u.userId = :userId " +
            "AND (:openingCodeFilter IS NULL OR LENGTH(:openingCodeFilter) = 0 OR g.opening.code = :openingCodeFilter) " +
            "AND (:player1FirstNameFilter IS NULL OR p1 IS NULL OR g.player1.firstName LIKE %:player1FirstNameFilter%) " +
            "AND (:player1LastNameFilter IS NULL OR p1 IS NULL OR g.player1.lastName LIKE %:player1LastNameFilter%) " +
            "AND (:player2FirstNameFilter IS NULL OR p2 IS NULL OR g.player2.firstName LIKE %:player2FirstNameFilter%) " +
            "AND (:player2LastNameFilter IS NULL OR p2 IS NULL OR g.player2.lastName LIKE %:player2LastNameFilter%) " +
            "AND (:resultFilter IS NULL OR LENGTH(:resultFilter) = 0 OR g.result = :resultFilter) " +
            "AND (:movesNumberMinFilter IS NULL OR g.movesNumber >= :movesNumberMinFilter) " +
            "AND (:movesNumberMaxFilter IS NULL OR g.movesNumber <= :movesNumberMaxFilter) " +
            "AND (:dateFromFilter IS NULL OR g.date >= :dateFromFilter) " +
            "AND (:dateToFilter IS NULL OR g.date <= :dateToFilter)"
    )
    Page <Game> findAllUsersFavoriteGamesWithFiltersPageable(
            @Param("userId") Long userId,
            @Param("openingCodeFilter") String openingCodeFilter,
            @Param("player1FirstNameFilter") String player1FirstNameFilter,
            @Param("player1LastNameFilter") String player1LastNameFilter,
            @Param("player2FirstNameFilter") String player2FirstNameFilter,
            @Param("player2LastNameFilter") String player2LastNameFilter,
            @Param("resultFilter") String resultFilter,
            @Param("movesNumberMinFilter") Integer movesNumberMinFilter,
            @Param("movesNumberMaxFilter") Integer movesNumberMaxFilter,
            @Param("dateFromFilter") LocalDate dateFromFilter,
            @Param("dateToFilter") LocalDate dateToFilter,
            Pageable pageable);

}
