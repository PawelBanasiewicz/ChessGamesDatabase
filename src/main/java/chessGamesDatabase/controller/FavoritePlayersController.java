package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Player;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.PlayerService;
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

import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.getPageRequest;

@Controller
@RequestMapping("/favorite-players")
public class FavoritePlayersController {
    private final UserService userService;
    private final PlayerService playerService;

    @Autowired
    public FavoritePlayersController(UserService userService, PlayerService playerService) {
        this.userService = userService;
        this.playerService = playerService;
    }

    @GetMapping("")
    public String displayFavoritePlayers(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "firstName", required = false) String sortField,
            @RequestParam(defaultValue = "asc", required = false) String sortDirection,
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

        Page<Player> favoritePlayersOnCurrentPage = playerService.findUsersFavoritePlayersWithFiltersPageable
                (user.getUserId(), firstNameFilter, lastNameFilter, birthDateFromFilter, birthDateToFilter,
                        sexFilter, eloMinFilter, eloMaxFilter,
                        getPageRequest(currentPage, ROWS_ON_NORMAL_PAGE, sortField, sortDirection));

        model.addAttribute("favoritePlayersOnCurrentPage", favoritePlayersOnCurrentPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("firstNameFilter", firstNameFilter);
        model.addAttribute("lastNameFilter", lastNameFilter);
        model.addAttribute("birthDateFromFilter", birthDateFromFilter);
        model.addAttribute("birthDateToFilter", birthDateToFilter);
        model.addAttribute("sexFilter", (sexFilter == null) ? null : String.valueOf(sexFilter));
        model.addAttribute("eloMinFilter", eloMinFilter);
        model.addAttribute("eloMaxFilter", eloMaxFilter);
        model.addAttribute("pageTitle", "Favorite players");

        return "player/favorite-players";
    }

    @PostMapping("/add")
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

    @GetMapping("/delete")
    public String deleteFavoritePlayer(@RequestParam("playerId")
                                       Long playerId,
                                       Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        Player player = playerService.findPlayerById(playerId);

        if (user != null && player != null) {
            user.deleteFavoritePlayer(player);
            userService.saveUser(user);
        }

        return "redirect:/favorite-players";
    }
}
