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

        return "player/players";
    }

    @GetMapping("/player/{playerId}")
    public String viewPlayer(@PathVariable Long playerId,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(required = false) String openingIdFilter,
                             @RequestParam(required = false) String player1FirstNameFilter,
                             @RequestParam(required = false) String player1LastNameFilter,
                             @RequestParam(required = false) String player2FirstNameFilter,
                             @RequestParam(required = false) String player2LastNameFilter,
                             @RequestParam(required = false) String resultFilter,
                             @RequestParam(required = false) Integer movesNumberMinFilter,
                             @RequestParam(required = false) Integer movesNumberMaxFilter,
                             @RequestParam(required = false) LocalDate dateFromFilter,
                             @RequestParam(required = false) LocalDate dateToFilter,
                             Model model) {

        Page<Game> actualPage;
        PageRequest pageRequest = PageRequest.of(page - 1, 20);

        if (openingIdFilter != null && !openingIdFilter.isEmpty() ||
                (player1FirstNameFilter != null && !player1FirstNameFilter.isEmpty()) ||
                (player1LastNameFilter != null && !player1LastNameFilter.isEmpty()) ||
                (player2FirstNameFilter != null && !player2FirstNameFilter.isEmpty()) ||
                (player2LastNameFilter != null && !player2LastNameFilter.isEmpty()) ||
                (resultFilter != null && !resultFilter.isEmpty()) ||
                (movesNumberMinFilter != null || movesNumberMaxFilter != null) ||
                (dateFromFilter != null || dateToFilter != null)) {

            if (openingIdFilter != null && openingIdFilter.isEmpty()) {
                openingIdFilter = null;
            }

            if (resultFilter != null && resultFilter.isEmpty()) {
                resultFilter = null;
            }
            actualPage = gameService.findAllGamesPlayedByPlayerWithFilters(playerId, openingIdFilter, player1FirstNameFilter, player1LastNameFilter,
                    player2FirstNameFilter, player2LastNameFilter, resultFilter, movesNumberMinFilter, movesNumberMaxFilter,
                    dateFromFilter, dateToFilter, pageRequest);
        } else {
            actualPage = gameService.findAllGamesPlayedByPlayer(playerId, pageRequest);
        }

        Player player = playerService.findPlayerById(playerId);

        model.addAttribute("player", player);
        model.addAttribute("actualPage", actualPage);
        model.addAttribute("openingIdFilter", openingIdFilter);
        model.addAttribute("player1FirstNameFilter", player1FirstNameFilter);
        model.addAttribute("player1LastNameFilter", player1LastNameFilter);
        model.addAttribute("player2FirstNameFilter", player2FirstNameFilter);
        model.addAttribute("player2LastNameFilter", player2LastNameFilter);
        model.addAttribute("resultFilter", resultFilter);
        model.addAttribute("movesNumberMinFilter", movesNumberMinFilter);
        model.addAttribute("movesNumberMaxFilter", movesNumberMaxFilter);
        model.addAttribute("dateFromFilter", dateFromFilter);
        model.addAttribute("dateToFilter", dateToFilter);
        return "player/player-details";
    }
}
