package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Role;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.RoleService;
import chessGamesDatabase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
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

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        List<User> users = userService.findUsersWithoutRole("ROLE_ADMINISTRATOR");
        model.addAttribute("users", users);
        return "admin/admin-panel";
    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam long userId, Model model) {
        User user = userService.findUserById(userId);
        List<Role> allRoles = roleService.findAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);

        return "admin/edit-user";
    }

    @PostMapping("/admin/save")
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


    @GetMapping("/admin/delete")
    public String deleteUser(@RequestParam long userId) {
        userService.deleteUserById(userId);

        return "redirect:/admin";
    }
}

