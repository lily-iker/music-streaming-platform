package music.utils;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SortUtil {
    public Sort resolveSortBy(String sortBy) {
        // Default sort by ID in ascending order
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("^(\\w+)(:)(asc|desc)$");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnToSort = matcher.group(1);
                String sortDirection = matcher.group(3);
                if (sortDirection.equalsIgnoreCase("asc")) {
                    sort = Sort.by(Sort.Direction.ASC, columnToSort);
                }
                else if (sortDirection.equalsIgnoreCase("desc")) {
                    sort = Sort.by(Sort.Direction.DESC, columnToSort);
                }
            }
        }

        return sort;
    }

    public Sort resolveSortBy(String sortBy, String defaultSortColumn) {
        Sort sort = Sort.by(Sort.Direction.DESC, defaultSortColumn);

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("^(\\w+)(:)(asc|desc)$");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnToSort = matcher.group(1);
                String sortDirection = matcher.group(3);
                if (sortDirection.equalsIgnoreCase("asc")) {
                    sort = Sort.by(Sort.Direction.ASC, columnToSort);
                }
                else if (sortDirection.equalsIgnoreCase("desc")) {
                    sort = Sort.by(Sort.Direction.DESC, columnToSort);
                }
            }
        }

        return sort;
    }
}
