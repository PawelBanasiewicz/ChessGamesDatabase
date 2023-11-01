package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.entity.Player;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.GameService;
import chessGamesDatabase.service.OpeningService;
import chessGamesDatabase.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.getPageRequest;
import static chessGamesDatabase.utils.Utils.addErrorMessageAndRedirect;

@Controller
@RequestMapping("/games")
public class GamesController {
    private static final Pattern lastNumberWithDotPattern = Pattern.compile("(\\d+\\.)(?=(?!.*\\d+\\.))");
    private final GameService gameService;
    private final PlayerService playerService;
    private final OpeningService openingService;

    @Autowired
    public GamesController(GameService gameService, PlayerService playerService, OpeningService openingService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.openingService = openingService;
    }

    @GetMapping("")
    public String games(@RequestParam(defaultValue = "1") int currentPage,
                        @RequestParam(defaultValue = "opening", required = false) String sortField,
                        @RequestParam(defaultValue = "asc", required = false) String sortDirection,
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
        Page<Game> gamesOnCurrentPage = gameService.findAllGamesWithFiltersPageable(
                openingIdFilter, player1FirstNameFilter, player1LastNameFilter,
                player2FirstNameFilter, player2LastNameFilter, resultFilter,
                movesNumberMinFilter, movesNumberMaxFilter, dateFromFilter,
                dateToFilter, getPageRequest(currentPage, ROWS_ON_NORMAL_PAGE,
                        sortField, sortDirection));


        model.addAttribute("gamesOnCurrentPage", gamesOnCurrentPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
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

    @GetMapping("/{gameId}")
    public String showGame(@PathVariable Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);

        model.addAttribute("game", game);
        model.addAttribute("pageTitle", "Game details");

        return "game/game-details";
    }

    @GetMapping("/addGame")
    public String showFormForAddGame(Model model) {
        Game game = new Game();

        model.addAttribute("game", game);
        model.addAttribute("pageTitle", "Add game");

        return "game/game-form";
    }

    @PostMapping("/save")
    public String saveGame(@ModelAttribute("game") Game game, RedirectAttributes redirectAttributes) {
        Player player1 = playerService.findPlayerByFirstNameAndLastName(
                game.getPlayer1().getFirstName(), game.getPlayer1().getLastName());
        Player player2 = playerService.findPlayerByFirstNameAndLastName(
                game.getPlayer2().getFirstName(), game.getPlayer2().getLastName());

        if (player1 == null || player2 == null || game.getPgn().isEmpty() ||
                game.getResult().isEmpty() || game.getDate() == null) {
            return addErrorMessageAndRedirectForGame(redirectAttributes);
        }

        game.setPlayer1(player1);
        game.setPlayer2(player2);

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

    @GetMapping("/update")
    public String showFormForUpdateGame(@RequestParam Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);

        model.addAttribute("game", game);
        model.addAttribute("pageTitle", "Edit game");

        return "game/game-form";
    }

    @GetMapping("/delete")
    public String deleteGame(@RequestParam Long gameId) {
        Game game = gameService.findGameById(gameId);
        List<User> followers = game.getFollowers();

        for (User user : followers) {
            user.deleteFavoriteGame(game);
        }

        gameService.deleteGameById(gameId);
        return "redirect:/games";
    }

    private static String addErrorMessageAndRedirectForGame(RedirectAttributes redirectAttributes) {
        return addErrorMessageAndRedirect(redirectAttributes, "game");
    }
}