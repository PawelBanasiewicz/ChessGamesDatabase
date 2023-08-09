package chessGamesDatabase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OpeningController {

    @GetMapping("/openings")
    public String openings() {
        return "openings";
    }
}
