package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.service.GameService;
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
public class GamesController {

    GameService gameService;

    @Autowired
    public GamesController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public String games(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(required = false) String openingCodeFilter,
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
        PageRequest pageRequest = PageRequest.of(page - 1, 30);

        if (openingCodeFilter != null && !openingCodeFilter.isEmpty() ||
                (player1FirstNameFilter != null && !player1FirstNameFilter.isEmpty()) ||
                (player1LastNameFilter != null && !player1LastNameFilter.isEmpty()) ||
                (player2FirstNameFilter != null && !player2FirstNameFilter.isEmpty()) ||
                (player2LastNameFilter != null && !player2LastNameFilter.isEmpty()) ||
                (resultFilter != null && !resultFilter.isEmpty()) ||
                (movesNumberMinFilter != null || movesNumberMaxFilter != null) ||
                (dateFromFilter != null || dateToFilter != null)) {

            if (openingCodeFilter != null && openingCodeFilter.isEmpty()) {
                openingCodeFilter = null;
            }

            if (resultFilter != null && resultFilter.isEmpty()) {
                resultFilter = null;
            }
            actualPage = gameService.findAllGamesWithFiltersPageable(openingCodeFilter, player1FirstNameFilter, player1LastNameFilter,
                    player2FirstNameFilter, player2LastNameFilter, resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageRequest);
        } else {
            actualPage = gameService.findAllGamesPageable(pageRequest);
        }

        model.addAttribute("actualPage", actualPage);
        model.addAttribute("openingCodeFilter", openingCodeFilter);
        model.addAttribute("player1FirstNameFilter", player1FirstNameFilter);
        model.addAttribute("player1LastNameFilter", player1LastNameFilter);
        model.addAttribute("player2FirstNameFilter", player2FirstNameFilter);
        model.addAttribute("player2LastNameFilter", player2LastNameFilter);
        model.addAttribute("resultFilter", resultFilter);
        model.addAttribute("movesNumberMinFilter", movesNumberMinFilter);
        model.addAttribute("movesNumberMaxFilter", movesNumberMaxFilter);
        model.addAttribute("dateFromFilter", dateFromFilter);
        model.addAttribute("dateToFilter", dateToFilter);

        return "games";
    }

    @GetMapping("/game/{gameId}")
    public String showGame(@PathVariable Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);
        model.addAttribute("game", game);

        return "game-details";
    }
}