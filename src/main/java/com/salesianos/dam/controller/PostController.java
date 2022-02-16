package com.salesianos.dam.controller;

import com.salesianos.dam.exception.PostNotFoundException;
import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Post.CreatePostDto;
import com.salesianos.dam.model.dto.Post.GetPostDto;
import com.salesianos.dam.model.dto.Post.PostDtoConverter;
import com.salesianos.dam.service.PostService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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


    @GetMapping("/public")
    public ResponseEntity<List<GetPostDto>> getAllPublic(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPublic());
    }

    @GetMapping("/me")
    public ResponseEntity<List<GetPostDto>> getUserPosts(@AuthenticationPrincipal Usuario currentUser){
        return  ResponseEntity.status(HttpStatus.OK).body(postService.getUserPosts(currentUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostDto> getPostById(@PathVariable Long id,@AuthenticationPrincipal Usuario currentUser){
        return ResponseEntity.status(HttpStatus.OK).body(postDtoConverter.postToGetPostDto(postService.getPostById(id, currentUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPostDto> edit(@RequestPart("file") MultipartFile file,
                                             @RequestPart("post") CreatePostDto editedPost,
                                             @PathVariable Long id) throws Exception {

        Optional<Post> postBuscado = postService.findById(id);


        if(postBuscado.isPresent()){

            Post postAEditar = postBuscado.get();

            return ResponseEntity.status(HttpStatus.OK).body(postDtoConverter.postToGetPostDto(postService.edit(postAEditar,editedPost,file)));

        }else {
            throw new PostNotFoundException ("Post no encontrado");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws IOException {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
