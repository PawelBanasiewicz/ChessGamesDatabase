package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.entity.Player;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.GameService;
import chessGamesDatabase.service.OpeningService;
import chessGamesDatabase.service.PlayerService;
import chessGamesDatabase.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chessGamesDatabase.utils.Utils.addErrorMessageAndRedirect;
import static chessGamesDatabase.utils.Utils.containsIgnoreCase;
import static chessGamesDatabase.utils.Utils.paginate;

@Controller
public class GamesController {
    private static Pattern lastNumberWithDotPattern;
    private final GameService gameService;
    private final UserService userService;
    private final PlayerService playerService;
    private final OpeningService openingService;

    @Autowired
    public GamesController(GameService gameService, UserService userService, PlayerService playerService, OpeningService openingService) {
        this.gameService = gameService;
        this.userService = userService;
        this.playerService = playerService;
        this.openingService = openingService;
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

    @GetMapping("/games/addGame")
    public String showFormForAddGame(Model model) {
        Game game = new Game();

        model.addAttribute("game", game);
        model.addAttribute("pageTitle", "Add game");

        return "game/game-form";
    }

    @PostMapping("games/save")
    public String saveGame(@ModelAttribute("game") Game game, RedirectAttributes redirectAttributes) {
        Player player1 = playerService.findPlayerByFirstNameAndLastName(game.getPlayer1().getFirstName(), game.getPlayer1().getLastName());
        Player player2 = playerService.findPlayerByFirstNameAndLastName(game.getPlayer2().getFirstName(), game.getPlayer2().getLastName());

        if (player1 == null || player2 == null || game.getPgn().isEmpty() || game.getResult().isEmpty() || game.getDate() == null) {
            return addErrorMessageAndRedirectForGame(redirectAttributes);
        }

        game.setPlayer1(player1);
        game.setPlayer2(player2);

        if (lastNumberWithDotPattern == null) {
            lastNumberWithDotPattern = Pattern.compile("(\\d+\\.)(?=(?!.*\\d+\\.))");
        }

        Matcher matcher = lastNumberWithDotPattern.matcher(game.getPgn());
        if (matcher.find()) {
            String lastNumberWithDot = matcher.group(1);
            String lastNumber = lastNumberWithDot.replace(".", "");
            game.setMovesNumber(Integer.parseInt(lastNumber));
        } else {
            return addErrorMessageAndRedirectForGame(redirectAttributes);
        }

        Opening opening = openingService.findOpeningByPgn(game.getPgn());
        if (opening == null) {
            return addErrorMessageAndRedirectForGame(redirectAttributes);
        }
        game.setOpening(opening);

        gameService.saveGame(game);
        return "redirect:/games";
    }

    @GetMapping("games/update")
    public String showFormForUpdateGame(@RequestParam Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);

        model.addAttribute("game", game);
        model.addAttribute("pageTitle", "Edit game");

        return "game/game-form";
    }

    @GetMapping("games/delete")
    public String deleteGame(@RequestParam Long gameId) {
        gameService.deleteGameById(gameId);
        return "redirect:/games";
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

    @PostMapping("/favorite-games/add")
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

    @GetMapping("/favorite-games/delete")
    public String deleteFavoriteGame(@RequestParam("gameId") Long gameId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        Game game = gameService.findGameById(gameId);

        if (user != null && game != null) {
            user.deleteFavoriteGame(game);
            userService.saveUser(user);
        }

        return "redirect:/favorite-games";
    }

    private static String addErrorMessageAndRedirectForGame(RedirectAttributes redirectAttributes) {
        return addErrorMessageAndRedirect(redirectAttributes, "game");
    }
}