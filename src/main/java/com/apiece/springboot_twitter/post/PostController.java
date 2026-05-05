package com.apiece.springboot_twitter.post;

import com.apiece.springboot_twitter.comment.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostRepository repository;

    @GetMapping("/search")
    public Slice<Post> searchList(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "3") int size
    ) {
        int page = 0;
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (lastId == null) {
            return repository.findSlicesBy(pageable);
        }

        return repository.findSlicesByIdLessThan(lastId,pageable);
    }


    @GetMapping("/api/posts")
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    @DeleteMapping("/api/posts/{id}")
    public void deletePost(@PathVariable long id) {
        repository.deleteById(id);
    }

    @PutMapping("/api/posts/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody Post postRequest) {
        return repository.findById(id)
                .map(post -> {
                    post.updateContent(postRequest.getContent());
                    return repository.save(post);
                }).orElseThrow();
    }

    @PostMapping("/api/posts")
    public ResponseEntity<Post> createPost(@RequestBody CommentRequest request) {
        Post newPost = Post.builder()
                .content(request.content())
                .build();

        Post savedPost = repository.save(newPost);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPost);
    }

    @GetMapping("/api/post/{id}")
    public Post getPost(@PathVariable long id) {
        return repository.findById(id)
                .orElseThrow();
    }
}
