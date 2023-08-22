package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Player;
import chessGamesDatabase.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class PlayersController {

    private final PlayerService playerService;

    @Autowired
    public PlayersController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public String players(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(required = false) String firstNameFilter,
                          @RequestParam(required = false) String lastNameFilter,
                          @RequestParam(required = false) LocalDate birthDateFromFilter,
                          @RequestParam(required = false) LocalDate birthDateToFilter,
                          @RequestParam(required = false) Character sexFilter,
                          @RequestParam(required = false) Integer eloMinFilter,
                          @RequestParam(required = false) Integer eloMaxFilter,
                          Model model) {

        Page<Player> actualPage;
        PageRequest pageRequest = PageRequest.of(page - 1, 30);

        if ((firstNameFilter != null && !firstNameFilter.isEmpty()) ||
                (lastNameFilter != null && !lastNameFilter.isEmpty()) ||
                (birthDateFromFilter != null || birthDateToFilter != null) ||
                (sexFilter != null) ||
                (eloMinFilter != null || eloMaxFilter != null)) {

            actualPage = playerService.findAllPlayersWithFiltersPageable(
                    firstNameFilter, lastNameFilter, birthDateFromFilter, birthDateToFilter,
                    sexFilter, eloMinFilter, eloMaxFilter,
                    pageRequest);
        } else {
            actualPage = playerService.findAllPlayersPageable(pageRequest);
        }

        model.addAttribute("actualPage", actualPage);
        model.addAttribute("firstNameFilter", firstNameFilter);
        model.addAttribute("lastNameFilter", lastNameFilter);
        model.addAttribute("birthDateFromFilter", birthDateFromFilter);
        model.addAttribute("birthDateToFilter", birthDateToFilter);
        model.addAttribute("sexFilter", sexFilter);
        model.addAttribute("eloMinFilter", eloMinFilter);
        model.addAttribute("eloMaxFilter", eloMaxFilter);

        return "players";
    }
}
