package chessGamesDatabase.dao;

import chessGamesDatabase.entity.Opening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningRepository extends JpaRepository<Opening, Long> {

}
