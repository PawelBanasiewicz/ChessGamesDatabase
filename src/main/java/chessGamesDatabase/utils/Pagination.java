package chessGamesDatabase.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class Pagination {
    public static int ROWS_ON_NORMAL_PAGE = 30;
    public static int ROWS_ON_DETAILS_PAGE = 20;

    private Pagination() {
    }

    public static PageRequest getPageRequest(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

    public static PageRequest getPageRequest(int page, int size, String sortField, String sortDestination) {
        return PageRequest.of(page - 1, size, createSort(sortField, sortDestination));
    }

    private static Sort createSort(String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Order.by(sortField));
        return sortDirection.equals("asc") ? sort.ascending() : sort.descending();
    }

    public static <T> Page<T> paginate(List<T> list, int page, int pageSize) {
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());

        List<T> subList = list.subList(start, end);

        return new PageImpl<>(subList, PageRequest.of(page - 1, pageSize), list.size());
    }
}
