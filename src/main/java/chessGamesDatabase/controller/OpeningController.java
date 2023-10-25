package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Opening;
import chessGamesDatabase.service.OpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/openings")
public class OpeningController {
    private final OpeningService openingService;

    @Autowired
    public OpeningController(OpeningService openingService) {
        this.openingService = openingService;
    }

    @GetMapping("")
    public String openings(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String codeFilter,
                           @RequestParam(required = false) String nameFilter,
                           @RequestParam(required = false) String pgnMovesFilter,
                           Model model) {

        Page<Opening> actualPage;
        PageRequest pageRequest = PageRequest.of(page - 1, 30);

        if ((codeFilter != null && !codeFilter.isEmpty()) ||
                (nameFilter != null && !nameFilter.isEmpty()) ||
                (pgnMovesFilter != null && !pgnMovesFilter.isEmpty())) {
            actualPage = openingService.findByCodeAndNameAndPgnMovesIgnoreCasePageable(codeFilter, nameFilter, pgnMovesFilter, pageRequest);

        } else {
            actualPage = openingService.findAllPageable(pageRequest);
        }

        model.addAttribute("actualPage", actualPage);
        model.addAttribute("codeFilter", codeFilter);
        model.addAttribute("nameFilter", nameFilter);
        model.addAttribute("pgnMovesFilter", pgnMovesFilter);
        model.addAttribute("pageTitle", "Openings");

        return "opening/openings";
    }

}
