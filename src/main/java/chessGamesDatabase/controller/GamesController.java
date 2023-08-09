package chessGamesDatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamesController {

    @GetMapping("/games")
    public String games() {
        return "games";
    }
}
