package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.service.OpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OpeningController {

    private final OpeningService openingService;

    @Autowired
    public OpeningController(OpeningService openingService) {
        this.openingService = openingService;
    }

    @GetMapping("/openings")
    public String openings(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String codeFilter,
                           @RequestParam(required = false) String nameFilter,
                           @RequestParam(required = false) String pgnMovesFilter,
                           Model model) {

        Page<Opening> actualPage;

        if (codeFilter != null && !codeFilter.isEmpty()) {
            actualPage = openingService.findByCodeContainingIgnoreCase(codeFilter, PageRequest.of(page - 1, 30));
        } else if (nameFilter != null && !nameFilter.isEmpty()) {
            actualPage = openingService.findByNameContainingIgnoreCase(nameFilter, PageRequest.of(page - 1, 30));
        } else if (pgnMovesFilter != null && !pgnMovesFilter.isEmpty()) {
            actualPage = openingService.findByPgnMovesContainingIgnoreCase(pgnMovesFilter, PageRequest.of(page - 1, 30));
        } else {
            actualPage = openingService.findAllPageable(PageRequest.of(page - 1, 30));
        }

        model.addAttribute("actualPage", actualPage);
        model.addAttribute("codeFilter", codeFilter);
        model.addAttribute("nameFilter", nameFilter);
        model.addAttribute("pgnMovesFilter", pgnMovesFilter);

        return "openings";
    }

}
