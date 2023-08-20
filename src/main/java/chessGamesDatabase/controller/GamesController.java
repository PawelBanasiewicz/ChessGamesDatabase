package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Game;
import chessGamesDatabase.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GamesController {

    GameService gameService;

    @Autowired
    public GamesController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public String games(@RequestParam(defaultValue = "1") int page,
            Model model) {

        Page<Game> actualPage = gameService.findAllPageable(PageRequest.of(page - 1, 30));
        model.addAttribute("actualPage", actualPage);

        return "games";
    }
}
