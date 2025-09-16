package com.kombuchagrande.dailyhabit.dto.review;

import java.util.List;

public record ReviewDtoCursorResponse(
    List<ReviewDto> content,
    Long lastIndex,
    boolean hasNext
) {

}
