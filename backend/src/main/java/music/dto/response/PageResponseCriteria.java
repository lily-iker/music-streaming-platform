package music.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class PageResponseCriteria<T> implements Serializable {
    private int offset;
    private int pageSize;
    private int totalElement;
    private T items;
}
