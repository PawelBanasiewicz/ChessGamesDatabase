package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.service.OpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.getPageRequest;

@Controller
@RequestMapping("/openings")
public class OpeningController {
    private final OpeningService openingService;

    @Autowired
    public OpeningController(OpeningService openingService) {
        this.openingService = openingService;
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
}
