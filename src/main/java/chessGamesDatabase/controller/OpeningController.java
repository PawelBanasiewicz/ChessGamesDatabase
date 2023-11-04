package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.service.GameService;
import chessGamesDatabase.service.OpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static chessGamesDatabase.utils.Pagination.ROWS_ON_DETAILS_PAGE;
import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.getPageRequest;

@Controller
@RequestMapping("/openings")
public class OpeningController {
    private final OpeningService openingService;

    private final GameService gameService;

    @Autowired
    public OpeningController(OpeningService openingService, GameService gameService) {
        this.openingService = openingService;
        this.gameService = gameService;
    }

    @GetMapping("")
    public String openings(@RequestParam(defaultValue = "1") int currentPage,
                           @RequestParam(defaultValue = "code", required = false) String sortField,
                           @RequestParam(defaultValue = "asc", required = false) String sortDirection,
                           @RequestParam(required = false) String codeFilter,
                           @RequestParam(required = false) String nameFilter,
                           @RequestParam(required = false) String pgnMovesFilter,
                           Model model) {

        Page<Opening> openingsOnCurrentPage = openingService.findAllOpeningsWithFiltersPageable(
                codeFilter, nameFilter, pgnMovesFilter,
                getPageRequest(currentPage, ROWS_ON_NORMAL_PAGE, sortField, sortDirection));

        model.addAttribute("openingsOnCurrentPage", openingsOnCurrentPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("codeFilter", codeFilter);
        model.addAttribute("nameFilter", nameFilter);
        model.addAttribute("pgnMovesFilter", pgnMovesFilter);
        model.addAttribute("pageTitle", "Openings");

        return "opening/openings";
    }

    @GetMapping("/{openingId}")
    public String showOpening(@PathVariable Long openingId,
                              @RequestParam(defaultValue = "1") int currentPage,
                              @RequestParam(defaultValue = "date", required = false) String sortField,
                              @RequestParam(defaultValue = "asc", required = false) String sortDirection,
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
        Opening opening = openingService.findOpeningById(openingId);
        Page<Game> gamesOnCurrentPage = gameService.findAllGamesStartedWithOpeningWithFiltersPageable(
                openingId, player1FirstNameFilter, player1LastNameFilter, player2FirstNameFilter,
                player2LastNameFilter, resultFilter, movesNumberMinFilter, movesNumberMaxFilter,
                dateFromFilter, dateToFilter,
                getPageRequest(currentPage, ROWS_ON_DETAILS_PAGE, sortField, sortDirection));

        model.addAttribute("opening", opening);
        model.addAttribute("gamesOnCurrentPage", gamesOnCurrentPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("player1FirstNameFilter", player1FirstNameFilter);
        model.addAttribute("player1LastNameFilter", player1LastNameFilter);
        model.addAttribute("player2FirstNameFilter", player2FirstNameFilter);
        model.addAttribute("player2LastNameFilter", player2LastNameFilter);
        model.addAttribute("resultFilter", resultFilter);
        model.addAttribute("movesNumberMinFilter", movesNumberMinFilter);
        model.addAttribute("movesNumberMaxFilter", movesNumberMaxFilter);
        model.addAttribute("dateFromFilter", dateFromFilter);
        model.addAttribute("dateToFilter", dateToFilter);
        model.addAttribute("PageTitle", "Opening Details");

        return "opening/opening-details";
    }
}
