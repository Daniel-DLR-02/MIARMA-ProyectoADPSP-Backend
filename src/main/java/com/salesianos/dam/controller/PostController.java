package com.salesianos.dam.controller;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Post.CreatePostDto;
import com.salesianos.dam.model.dto.Post.GetPostDto;
import com.salesianos.dam.model.dto.Post.PostDtoConverter;
import com.salesianos.dam.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostDtoConverter postDtoConverter;

    @PostMapping("/")
    public ResponseEntity<GetPostDto> create(@RequestPart("file") MultipartFile file,
                                             @RequestPart("post") CreatePostDto newPost,
                                             @AuthenticationPrincipal Usuario currentUser) throws Exception {

        Post saved = postService.save(newPost,file,currentUser);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(postDtoConverter.postToGetPostDto(saved));



    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws IOException {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
