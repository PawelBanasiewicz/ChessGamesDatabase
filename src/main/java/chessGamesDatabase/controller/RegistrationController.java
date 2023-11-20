package chessGamesDatabase.controller;

import chessGamesDatabase.entity.Role;
import chessGamesDatabase.entity.User;
import chessGamesDatabase.service.RoleService;
import chessGamesDatabase.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, RoleService roleService,
                                  PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Sign up");
        return "registration/sign-up-form";
    }

    @PostMapping("/send-confirmation")
    public String sendConfirmationEmail(@ModelAttribute("user") User user,
                                        @RequestParam("repeatedPassword") String repeatedPassword,
                                        Model model) {
        String username = user.getUsername();
        String email = user.getEmail();
        boolean isValidData = true;

        if (userService.findUserByUsername(username) != null) {
            model.addAttribute("usernameError", "This username already exists.");
            isValidData = false;
        }

        if (userService.findUserByEmail(email) != null) {
            model.addAttribute("emailError", "This email already exists.");
            isValidData = false;
        }

        if (!user.getPassword().equals(repeatedPassword)) {
            model.addAttribute("passwordError", "Passwords don't match.");
            isValidData = false;
        }

        if (isValidData) {
            List<Role> roles = new ArrayList<>();
            Role roleUser = roleService.findRoleByRoleName("ROLE_USER");
            roles.add(roleUser);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(roles);
            user.setEnabled(true);

            userService.saveUser(user);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("pageTitle", "Email confirmation request");
            return "registration/sing-up-confirmation";
        }

        model.addAttribute("pageTitle", "Registration Form");
        return "registration/sign-up-form";
    }
}
