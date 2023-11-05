package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.OpeningService;
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

import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.getPageRequest;

@Controller
@RequestMapping("/favorite-openings")
public class FavoriteOpeningsController {
    private final OpeningService openingService;
    private final UserService userService;

    @Autowired
    public FavoriteOpeningsController(OpeningService openingService, UserService userService) {
        this.openingService = openingService;
        this.userService = userService;
    }

    @GetMapping("")
    public String displayFavoriteOpenings(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "code", required = false) String sortField,
            @RequestParam(defaultValue = "asc", required = false) String sortDirection,
            @RequestParam(required = false) String codeFilter,
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) String pgnMovesFilter,
            Authentication authentication,
            Model model) {
        User user = userService.findUserByUsername(authentication.getName());

        Page<Opening> favoriteOpeningsOnCurrentPage = openingService.findUsersFavoriteOpeningsWithFiltersPageable(
                user.getUserId(), codeFilter, nameFilter, pgnMovesFilter,
                getPageRequest(currentPage, ROWS_ON_NORMAL_PAGE, sortField, sortDirection));

        model.addAttribute("favoriteOpeningsOnCurrentPage", favoriteOpeningsOnCurrentPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("codeFilter", codeFilter);
        model.addAttribute("nameFilter", nameFilter);
        model.addAttribute("pgnMovesFilter", pgnMovesFilter);
        model.addAttribute("pageTitle", "Favorite openings");

        return "opening/favorite-openings";
    }

    @PostMapping("/add")
    public String addFavoriteOpening(@RequestParam("openingId") Long openingId,
                                     Authentication authentication,
                                     HttpServletRequest httpServletRequest) {
        User user = userService.findUserByUsername(authentication.getName());
        Opening opening = openingService.findOpeningById(openingId);

        if (user != null && opening != null && !user.getFavoriteOpenings().contains(opening)) {
            user.addFavoriteOpening(opening);
            userService.saveUser(user);
        }

        String referer = httpServletRequest.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        return "redirect:/openings";
    }

    @GetMapping("/delete")
    public String deleteFavoriteOpening(@RequestParam("openingId") Long openingId,
                                        Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        Opening opening = openingService.findOpeningById(openingId);

        if (user != null && opening != null) {
            user.deleteFavoriteOpening(opening);
            userService.saveUser(user);
        }

        return "redirect:/favorite-openings";
    }
}
