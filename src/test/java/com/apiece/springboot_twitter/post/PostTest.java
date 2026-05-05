package com.apiece.springboot_twitter.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void increaseCommentCount() {
        Post post = Post.builder().build();
        post.increaseCommentCount();

        Assertions.assertThat(post.getCommentCount()).isEqualTo(1);
    }

}