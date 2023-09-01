package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.Player;
import chessGamesDatabase.service.GameService;
import chessGamesDatabase.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class PlayersController {

    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public PlayersController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @GetMapping("/players")
    public String players(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(required = false) String firstNameFilter,
                          @RequestParam(required = false) String lastNameFilter,
                          @RequestParam(required = false) LocalDate birthDateFromFilter,
                          @RequestParam(required = false) LocalDate birthDateToFilter,
                          @RequestParam(required = false) Character sexFilter,
                          @RequestParam(required = false) Integer eloMinFilter,
                          @RequestParam(required = false) Integer eloMaxFilter,
                          Model model) {

        Page<Player> actualPage;
        PageRequest pageRequest = PageRequest.of(page - 1, 30);

        if ((firstNameFilter != null && !firstNameFilter.isEmpty()) ||
                (lastNameFilter != null && !lastNameFilter.isEmpty()) ||
                (birthDateFromFilter != null || birthDateToFilter != null) ||
                (sexFilter != null) ||
                (eloMinFilter != null || eloMaxFilter != null)) {

            actualPage = playerService.findAllPlayersWithFiltersPageable(
                    firstNameFilter, lastNameFilter, birthDateFromFilter, birthDateToFilter,
                    sexFilter, eloMinFilter, eloMaxFilter,
                    pageRequest);
        } else {
            actualPage = playerService.findAllPlayersPageable(pageRequest);
        }

        model.addAttribute("actualPage", actualPage);
        model.addAttribute("firstNameFilter", firstNameFilter);
        model.addAttribute("lastNameFilter", lastNameFilter);
        model.addAttribute("birthDateFromFilter", birthDateFromFilter);
        model.addAttribute("birthDateToFilter", birthDateToFilter);
        model.addAttribute("sexFilter", sexFilter);
        model.addAttribute("eloMinFilter", eloMinFilter);
        model.addAttribute("eloMaxFilter", eloMaxFilter);

        return "players";
    }

    @GetMapping("/player/{playerId}")
    public String viewPlayer(@PathVariable Long playerId,
                             @RequestParam(defaultValue = "1") int page,
                             Model model) {
        Player player = playerService.findPlayerById(playerId);
        Page<Game> actualPage = gameService.findAllGamesPlayedByPlayer(playerId, PageRequest.of(page - 1, 20));

        model.addAttribute("player", player);
        model.addAttribute("actualPage", actualPage);
        return "player-details";
    }
}
