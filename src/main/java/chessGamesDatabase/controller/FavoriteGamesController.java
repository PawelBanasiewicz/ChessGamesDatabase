package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.GameService;
import chessGamesDatabase.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.paginate;
import static chessGamesDatabase.utils.Utils.containsIgnoreCase;

@Controller
@RequestMapping("/favorite-games")
public class FavoriteGamesController {
    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public FavoriteGamesController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("")
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

        Page<Game> actualPage = paginate(favoriteGames, page, ROWS_ON_NORMAL_PAGE);

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

    @PostMapping("/add")
    public String addFavoriteGame(@RequestParam("gameId") Long gameId,
                                  Authentication authentication,
                                  HttpServletRequest httpServletRequest) {
        User user = userService.findUserByUsername(authentication.getName());
        Game game = gameService.findGameById(gameId);

        if (user != null && game != null && !user.getFavoriteGames().contains(game)) {
            user.addFavoriteGame(game);
            userService.saveUser(user);
        }

        String referer = httpServletRequest.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        return "redirect:/games";
    }

    @GetMapping("/delete")
    public String deleteFavoriteGame(@RequestParam("gameId") Long gameId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        Game game = gameService.findGameById(gameId);

        if (user != null && game != null) {
            user.deleteFavoriteGame(game);
            userService.saveUser(user);
        }

        return "redirect:/favorite-games";
    }
}
