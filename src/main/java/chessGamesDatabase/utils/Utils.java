package chessGamesDatabase.utils;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class Utils {
    private Utils() {
    }

    public static String addErrorMessageAndRedirect(RedirectAttributes redirectAttributes, String savingObjectName) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Failed. Check that the fields you filled in are correct");
        return "redirect:/" + savingObjectName + "s";
    }

    public static boolean containsIgnoreCase(String string1, String string2) {
        return (string1.toLowerCase().contains(string2.toLowerCase()));
    }
}
