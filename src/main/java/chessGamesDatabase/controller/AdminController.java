package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Role;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.RoleService;
import chessGamesDatabase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import static chessGamesDatabase.utils.Pagination.ROWS_ON_NORMAL_PAGE;
import static chessGamesDatabase.utils.Pagination.getPageRequest;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String adminPanel(@RequestParam(defaultValue = "1") int currentPage,
                             @RequestParam(required = false) String usernameFilter,
                             @RequestParam(required = false) String emailFilter,
                             @RequestParam(required = false) Boolean enabledFilter,
                             @RequestParam(required = false) LocalDateTime createdDateFromFilter,
                             @RequestParam(required = false) LocalDateTime createdDateToFilter,
                             @RequestParam(required = false) LocalDateTime lastTimeLoginDateFromFilter,
                             @RequestParam(required = false) LocalDateTime lastTimeLoginDateToFilter,
                             Model model) {

        Page<User> usersOnCurrentPage = userService.findUsersWithoutRoleWithFiltersPageable(
                "ROLE_ADMINISTRATOR", usernameFilter, emailFilter, enabledFilter,
                createdDateFromFilter, createdDateToFilter, lastTimeLoginDateFromFilter,
                lastTimeLoginDateToFilter, getPageRequest(currentPage, ROWS_ON_NORMAL_PAGE));

        model.addAttribute("usersOnCurrentPage", usersOnCurrentPage);
        model.addAttribute("usernameFilter", usernameFilter);
        model.addAttribute("emailFilter", emailFilter);
        model.addAttribute("enabledFilter", enabledFilter);
        model.addAttribute("createdDateFromFilter", createdDateFromFilter);
        model.addAttribute("createdDateToFilter", createdDateToFilter);
        model.addAttribute("lastTimeLoginDateFromFilter", lastTimeLoginDateFromFilter);
        model.addAttribute("lastTimeLoginDateToFilter", lastTimeLoginDateToFilter);
        model.addAttribute("pageTitle", "Admin panel");

        return "admin/admin-panel";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam long userId, Model model) {
        User user = userService.findUserById(userId);
        List<Role> allRoles = roleService.findAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("pageTitle", "Edit user");

        return "admin/edit-user";
    }

    @PostMapping("/save")
    public String saveEditedUser(@ModelAttribute User editedUser,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("roles") List<Role> chosenRoles) {
        User originalUser = userService.findUserById(editedUser.getUserId());
        editedUser.setCreatedAt(originalUser.getCreatedAt());
        editedUser.setLastLogin(originalUser.getLastLogin());
        editedUser.setRoles(chosenRoles);

        if (!newPassword.isEmpty()) {
            editedUser.setPassword(passwordEncoder.encode(newPassword));
        } else {
            editedUser.setPassword(originalUser.getPassword());
        }

        userService.saveUser(editedUser);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin";
    }
}

