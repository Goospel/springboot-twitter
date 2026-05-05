package com.apiece.springboot_twitter.comment;

import com.apiece.springboot_twitter.post.Post;
import com.apiece.springboot_twitter.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final RepositoryMethodInvocationListener repositoryMethodInvocationListener;

    public CommentResponse createComment(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .id(null)
                .post(post)
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Comment newComment = commentRepository.save(comment);

        post.increaseCommentCount();
        postRepository.save(post);

        return CommentResponse.from(newComment);
    }

    public List<CommentResponse> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByIdDesc(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    public CommentResponse updateComment(Long postId, Long commentId, CommentRequest updateContent) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new IllegalStateException("조회 실패"));

        comment.updateContnet(updateContent.content());
        return CommentResponse.from(commentRepository.save(comment));
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new IllegalStateException("조회 실패"));

        Post post = comment.getPost();

        commentRepository.delete(comment);

        post.decreaseCommentCount();
        postRepository.save(post);
    }

    public List<CommentResponse> getAllComments() {
        return commentRepository.findAllWithPostBy()
                .stream()
                .map(comment ->
                {
                    Post post = comment.getPost();
                    log.info("댓글 ID: {}, 게시글 ID: {}, 게시글 내용: {}"
                            , comment.getId(), post.getId(), post.getContent());
                    return CommentResponse.from(comment);
                })
                .toList();
    }
}
