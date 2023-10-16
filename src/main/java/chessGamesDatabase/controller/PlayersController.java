package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.Player;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.GameService;
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

import static chessGamesDatabase.utils.Utils.addErrorMessageAndRedirect;
import static chessGamesDatabase.utils.Utils.containsIgnoreCase;
import static chessGamesDatabase.utils.Utils.paginate;

@Controller
public class PlayersController {

    private final PlayerService playerService;
    private final GameService gameService;

    private final UserService userService;

    @Autowired
    public PlayersController(PlayerService playerService, GameService gameService, UserService userService) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.userService = userService;
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
        model.addAttribute("pageTitle", "Players");

        return "player/players";
    }

    @GetMapping("/players/{playerId}")
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
        model.addAttribute("pageTitle", "Player details");

        return "player/player-details";
    }

    @GetMapping("players/addPlayer")
    public String showFormForAddPlayer(Model model) {
        Player player = new Player();

        model.addAttribute("player", player);
        model.addAttribute("pageTitle", "Add player");

        return "player/player-form";
    }

    @PostMapping("players/save")
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

    @GetMapping("players/update")
    public String showFormForUpdatePlayer(@RequestParam Long playerId, Model model) {
        Player player = playerService.findPlayerById(playerId);

        model.addAttribute("player", player);
        model.addAttribute("pageTitle", "Edit player");

        return "player/player-form";
    }

    @GetMapping("players/delete")
    public String deletePlayer(@RequestParam Long playerId, Model model) {
        playerService.deletePlayerById(playerId);
        return "redirect:/players";
    }


    @GetMapping("/favorite-players")
    public String displayFavoritePlayers(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(required = false) String firstNameFilter,
                                         @RequestParam(required = false) String lastNameFilter,
                                         @RequestParam(required = false) LocalDate birthDateFromFilter,
                                         @RequestParam(required = false) LocalDate birthDateToFilter,
                                         @RequestParam(required = false) Character sexFilter,
                                         @RequestParam(required = false) Integer eloMinFilter,
                                         @RequestParam(required = false) Integer eloMaxFilter,
                                         Authentication authentication,
                                         Model model) {

        User user = userService.findUserByUsername(authentication.getName());

        List<Player> favoritePlayers = user.getFavoritePlayers().stream()
                .filter(player ->
                        ((firstNameFilter == null || firstNameFilter.isEmpty()) || containsIgnoreCase(player.getFirstName(), firstNameFilter)) &&
                                ((lastNameFilter == null || lastNameFilter.isEmpty()) || containsIgnoreCase(player.getLastName(), lastNameFilter)) &&
                                (birthDateFromFilter == null || !player.getBirthDate().isBefore(birthDateFromFilter)) &&
                                (birthDateToFilter == null || !player.getBirthDate().isAfter(birthDateToFilter)) &&
                                (sexFilter == null || player.getSex() == sexFilter) &&
                                (eloMinFilter == null || player.getElo() > eloMinFilter) &&
                                (eloMaxFilter == null || player.getElo() < eloMaxFilter)
                )
                .toList();

        Page<Player> actualPage = paginate(favoritePlayers, page, 30);

        model.addAttribute("actualPage", actualPage);
        model.addAttribute("firstNameFilter", firstNameFilter);
        model.addAttribute("lastNameFilter", lastNameFilter);
        model.addAttribute("birthDateFromFilter", birthDateFromFilter);
        model.addAttribute("birthDateToFilter", birthDateToFilter);
        model.addAttribute("sexFilter", sexFilter);
        model.addAttribute("eloMinFilter", eloMinFilter);
        model.addAttribute("eloMaxFilter", eloMaxFilter);
        model.addAttribute("pageTitle", "Favorite players");

        return "player/favorite-players";
    }

    @PostMapping("/favorite-players/add")
    public String addFavoritePlayer(@RequestParam("playerId") Long playerId,
                                    Authentication authentication,
                                    HttpServletRequest httpServletRequest) {
        User user = userService.findUserByUsername(authentication.getName());
        Player player = playerService.findPlayerById(playerId);

        if (user != null && player != null && !user.getFavoritePlayers().contains(player)) {
            user.addFavoritePlayer(player);
            userService.saveUser(user);
        }

        String referer = httpServletRequest.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        return "redirect:/favorite-players";
    }

    @GetMapping("/favorite-players/delete")
    public String deleteFavoritePlayer(@RequestParam("playerId") Long playerId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        Player player = playerService.findPlayerById(playerId);

        if (user != null && player != null) {
            user.deleteFavoritePlayer(player);
            userService.saveUser(user);
        }

        return "redirect:/favorite-players";
    }

    private static String addErrorMessageAndRedirectForPlayer(RedirectAttributes redirectAttributes) {
        return addErrorMessageAndRedirect(redirectAttributes, "player");
    }
}
