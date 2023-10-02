package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT p FROM Player p " +
            "WHERE (:firstNameFilter IS NULL OR p.firstName LIKE %:firstNameFilter%) " +
            "AND (:lastNameFilter IS NULL OR p.lastName LIKE %:lastNameFilter%) " +
            "AND (:birthDateFromFilter IS NULL OR p.birthDate >= :birthDateFromFilter) " +
            "AND (:birthDateToFilter IS NULL OR p.birthDate <= :birthDateToFilter) " +
            "AND (:sexFilter IS NULL OR p.sex = :sexFilter) " +
            "AND (:eloMinFilter IS NULL OR p.elo >= :eloMinFilter) " +
            "AND (:eloMaxFilter IS NULL OR p.elo <= :eloMaxFilter)")
    Page<Player> findAllPlayersWithFilters(
            @Param("firstNameFilter") String firstNameFilter,
            @Param("lastNameFilter") String lastNameFilter,
            @Param("birthDateFromFilter") LocalDate birthDateFromFilter,
            @Param("birthDateToFilter") LocalDate birthDateToFilter,
            @Param("sexFilter") Character sexFilter,
            @Param("eloMinFilter") Integer eloMinFilter,
            @Param("eloMaxFilter") Integer eloMaxFilter,
            Pageable pageable);

    @Query("SELECT p FROM Player p WHERE p.firstName = :firstName AND p.lastName = :lastName")
    Player findPlayerByFirstNameAndLastName(@Param("firstName") String firstName,
                                            @Param("lastName") String lastName);
}
