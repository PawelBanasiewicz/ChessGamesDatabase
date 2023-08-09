package chessGamesDatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlayersController {

    @GetMapping("/players")
    public String players() {
        return "players";
    }
}
