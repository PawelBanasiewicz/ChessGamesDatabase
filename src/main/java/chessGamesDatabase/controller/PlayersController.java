package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.Player;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.GameService;
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

import static chessGamesDatabase.utils.Utils.addErrorMessageAndRedirect;
import static chessGamesDatabase.utils.Utils.getPageRequest;

@Controller
@RequestMapping("/players")
public class PlayersController {
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public PlayersController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @GetMapping("")
    public String players(@RequestParam(defaultValue = "1") int currentPage,
                          @RequestParam(required = false) String firstNameFilter,
                          @RequestParam(required = false) String lastNameFilter,
                          @RequestParam(required = false) LocalDate birthDateFromFilter,
                          @RequestParam(required = false) LocalDate birthDateToFilter,
                          @RequestParam(required = false) Character sexFilter,
                          @RequestParam(required = false) Integer eloMinFilter,
                          @RequestParam(required = false) Integer eloMaxFilter,
                          Model model) {
        Page<Player> playersOnCurrentPage = playerService.findAllPlayersWithFiltersPageable(
                firstNameFilter, lastNameFilter, birthDateFromFilter, birthDateToFilter,
                sexFilter, eloMinFilter, eloMaxFilter,
                getPageRequest(currentPage, 30));

        model.addAttribute("actualPage", playersOnCurrentPage);
        model.addAttribute("firstNameFilter", firstNameFilter);
        model.addAttribute("lastNameFilter", lastNameFilter);
        model.addAttribute("birthDateFromFilter", birthDateFromFilter);
        model.addAttribute("birthDateToFilter", birthDateToFilter);
        model.addAttribute("sexFilter", sexFilter);
        model.addAttribute("eloMinFilter", eloMinFilter);
        model.addAttribute("eloMaxFilter", eloMaxFilter);
        model.addAttribute("pageTitle", "Players");

        return "player/players";
    }

    @GetMapping("/{playerId}")
    public String viewPlayer(@PathVariable Long playerId,
                             @RequestParam(defaultValue = "1") int currentPage,
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
        Page<Game> gamesOnCurrentPage = gameService.findAllGamesPlayedByPlayerWithFiltersPageable(
                playerId, openingIdFilter, player1FirstNameFilter, player1LastNameFilter,
                player2FirstNameFilter, player2LastNameFilter, resultFilter, movesNumberMinFilter,
                movesNumberMaxFilter, dateFromFilter, dateToFilter, getPageRequest(currentPage, 20));

        Player player = playerService.findPlayerById(playerId);

        model.addAttribute("player", player);
        model.addAttribute("gamesOnCurrentPage", gamesOnCurrentPage);
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
        model.addAttribute("pageTitle", "Player details");

        return "player/player-details";
    }

    @GetMapping("/addPlayer")
    public String showFormForAddPlayer(Model model) {
        Player player = new Player();

        model.addAttribute("player", player);
        model.addAttribute("pageTitle", "Add player");

        return "player/player-form";
    }

    @PostMapping("/save")
    public String savePlayer(@ModelAttribute("player") Player player, RedirectAttributes redirectAttributes) {
        Player existingPlayer = playerService.findPlayerByFirstNameAndLastName(player.getFirstName(), player.getLastName());

        if (player.getPlayerId() == 0) {
            if (existingPlayer != null) {
                return addErrorMessageAndRedirectForPlayer(redirectAttributes);
            }
            playerService.savePlayer(player);
        } else {
            Player editedPlayer = playerService.findPlayerById(player.getPlayerId());
            if (existingPlayer == null || editedPlayer.getPlayerId() == existingPlayer.getPlayerId()) {
                editedPlayer.setFirstName(player.getFirstName());
                editedPlayer.setLastName(player.getLastName());
                editedPlayer.setBirthDate(player.getBirthDate());
                editedPlayer.setSex(player.getSex());
                editedPlayer.setElo(player.getElo());
                playerService.savePlayer(editedPlayer);
            } else {
                return addErrorMessageAndRedirectForPlayer(redirectAttributes);
            }

        }
        return "redirect:/players";
    }

    @GetMapping("/update")
    public String showFormForUpdatePlayer(@RequestParam Long playerId, Model model) {
        Player player = playerService.findPlayerById(playerId);

        model.addAttribute("player", player);
        model.addAttribute("pageTitle", "Edit player");

        return "player/player-form";
    }

    @GetMapping("/delete")
    public String deletePlayer(@RequestParam Long playerId, Model model) {
        Player player = playerService.findPlayerById(playerId);

        List<Game> allGamesPlayedByPlayer = gameService.findAllGamesPlayedByPlayer(playerId);
        for (Game game : allGamesPlayedByPlayer) {
            if (game.getPlayer1() != null && game.getPlayer1().getPlayerId() == playerId) {
                game.setPlayer1(null);
            }
            if (game.getPlayer2() != null && game.getPlayer2().getPlayerId() == playerId) {
                game.setPlayer2(null);
            }
        }

        List<User> users = player.getFollowers();
        for (User user : users) {
            user.deleteFavoritePlayer(player);
        }

        playerService.deletePlayerById(playerId);
        return "redirect:/players";
    }

    private static String addErrorMessageAndRedirectForPlayer(RedirectAttributes redirectAttributes) {
        return addErrorMessageAndRedirect(redirectAttributes, "player");
    }
}
