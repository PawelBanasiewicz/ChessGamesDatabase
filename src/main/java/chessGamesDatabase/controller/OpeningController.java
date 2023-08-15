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
    public String openings(@RequestParam(defaultValue = "1") int page, Model model) {
        Page<Opening> actualPage = openingService.findAllPageable(PageRequest.of(page - 1, 30));
        model.addAttribute("actualPage", actualPage);
        return "openings";
    }

}
