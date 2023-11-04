package chessGamesDatabase.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Pagination {
    public static int ROWS_ON_NORMAL_PAGE = 30;
    public static int ROWS_ON_DETAILS_PAGE = 20;

    private Pagination() {
    }

    public static PageRequest getPageRequest(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    public static PageRequest getPageRequest(int page, int size, String sortField, String sortDirection) {
        return PageRequest.of(page - 1, size, createSort(sortField, sortDirection));
    }

    private static Sort createSort(String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Order.by(sortField));
        return sortDirection.equals("asc") ? sort.ascending() : sort.descending();
    }
}
