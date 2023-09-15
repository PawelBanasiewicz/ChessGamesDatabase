package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.GameService;
import chessGamesDatabase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static chessGamesDatabase.utils.Utils.containsIgnoreCase;
import static chessGamesDatabase.utils.Utils.paginate;

@Controller
public class GamesController {

    GameService gameService;
    UserService userService;

    @Autowired
    public GamesController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/games")
    public String games(@RequestParam(defaultValue = "1") int page,
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
        PageRequest pageRequest = PageRequest.of(page - 1, 30);

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
            actualPage = gameService.findAllGamesWithFiltersPageable(openingIdFilter, player1FirstNameFilter, player1LastNameFilter,
                    player2FirstNameFilter, player2LastNameFilter, resultFilter, movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter, dateToFilter, pageRequest);
        } else {
            actualPage = gameService.findAllGamesPageable(pageRequest);
        }

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
        model.addAttribute("pageTitle", "Games");

        return "game/games";
    }

    @GetMapping("/games/{gameId}")
    public String showGame(@PathVariable Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);

        model.addAttribute("game", game);
        model.addAttribute("pageTitle", "Game details");

        return "game/game-details";
    }

    @GetMapping("/favorite-games")
    public String displayFavoriteGames(@RequestParam(defaultValue = "1") int page,
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
                                       Authentication authentication,
                                       Model model) {
        User user = userService.findUserByUsername(authentication.getName());
        List<Game> favoriteGames = user.getFavoriteGames().stream()
                .filter(game ->
                        ((openingIdFilter == null || openingIdFilter.isEmpty()) || game.getOpening().getCode().equalsIgnoreCase(openingIdFilter)) &&
                                ((player1FirstNameFilter == null || player1FirstNameFilter.isEmpty()) || containsIgnoreCase(game.getPlayer1().getFirstName(), player1FirstNameFilter)) &&
                                ((player1LastNameFilter == null || player1LastNameFilter.isEmpty()) || containsIgnoreCase(game.getPlayer1().getLastName(), player1LastNameFilter)) &&
                                ((player2FirstNameFilter == null || player2FirstNameFilter.isEmpty()) || containsIgnoreCase(game.getPlayer2().getFirstName(), player2FirstNameFilter)) &&
                                ((player2LastNameFilter == null || player2LastNameFilter.isEmpty()) || containsIgnoreCase(game.getPlayer2().getLastName(), player2LastNameFilter)) &&
                                ((resultFilter == null || resultFilter.isEmpty()) || game.getResult().equalsIgnoreCase(resultFilter)) &&
                                (movesNumberMinFilter == null || game.getMovesNumber() > movesNumberMinFilter) &&
                                (movesNumberMaxFilter == null || game.getMovesNumber() < movesNumberMaxFilter) &&
                                (dateFromFilter == null || !game.getDate().isBefore(dateFromFilter)) &&
                                (dateToFilter == null || !game.getDate().isAfter(dateToFilter))
                )
                .toList();

        Page<Game> actualPage = paginate(favoriteGames, page, 30);

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
        model.addAttribute("pageTitle", "favorite games");

        return "game/favorite-games";
    }

    @GetMapping("/favorite-games/delete")
    public String removeFavoriteGame(@RequestParam("gameId") Long gameId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        Game game = gameService.findGameById(gameId);

        if (user != null && game != null) {
            user.removeFavoriteGame(game);
            userService.saveUser(user);
        }

        return "redirect:/favorite-games";
    }
}