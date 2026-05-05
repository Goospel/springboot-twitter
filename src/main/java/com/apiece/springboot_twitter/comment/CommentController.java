package com.apiece.springboot_twitter.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public CommentResponse create(
            @PathVariable Long postId,
            @Validated @RequestBody CommentRequest commentRequest
    ) {
        return commentService.createComment(postId, commentRequest);
    }

    @GetMapping("/api/posts/{postId}/comments")
    public List<CommentResponse> getComment(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }

    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public CommentResponse updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Validated @RequestBody CommentRequest content
    ) {
        return commentService.updateComment(postId, commentId, content);
    }

    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public void deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(postId, commentId);
    }

    @GetMapping("/api/comments")
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }
}
