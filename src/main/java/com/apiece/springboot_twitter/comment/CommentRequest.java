package com.apiece.springboot_twitter.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 1, max = 50, message = "댓글은 1자 이상 그리고 50글자 이하여야 합니다.")
        String content
) {
}
