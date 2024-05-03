package carrot.candy.app.common.dto;

import java.util.List;

public record SliceResponse<T> (
        List<T> values,
        Boolean hasNext,
        String cursor
) {

    public static <T> SliceResponse<T> of(
            List<T> values,
            Boolean hasNext,
            String cursor
    ) {
        return new SliceResponse(
                values,
                hasNext,
                cursor
        );
    }
}
