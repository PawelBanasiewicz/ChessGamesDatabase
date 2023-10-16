package chessGamesDatabase.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Utils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Utils() {
    }

    public static <T> Page<T> paginate(List<T> list, int page, int pageSize) {
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());

        List<T> subList = list.subList(start, end);

        return new PageImpl<>(subList, PageRequest.of(page - 1, pageSize), list.size());
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
