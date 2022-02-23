package com.salesianos.dam.controller;

import com.salesianos.dam.errors.exception.PostNotFoundException;
import com.salesianos.dam.errors.exception.UnauthorizedRequestException;
import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Post.CreatePostDto;
import com.salesianos.dam.model.dto.Post.GetPostDto;
import com.salesianos.dam.model.dto.Post.PostDtoConverter;
import com.salesianos.dam.service.PostService;
import com.salesianos.dam.utils.pagination.PaginationLinksUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostDtoConverter postDtoConverter;
    private final PaginationLinksUtils paginationLinksUtils;

    @PostMapping("/")
    public ResponseEntity<GetPostDto> create(@RequestPart("file") MultipartFile file,
                                             @RequestPart("post") @Valid CreatePostDto newPost,
                                             @AuthenticationPrincipal Usuario currentUser) throws Exception {

        Post saved = postService.save(newPost,file,currentUser);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(postDtoConverter.postToGetPostDto(saved));



    }

    @PostMapping("/{nick}")
    public ResponseEntity<?> getPostsOfUserWithNick(@PageableDefault(size = 10,page=0) Pageable pageable,@PathVariable String nick,
                                                                   @AuthenticationPrincipal Usuario currentUser){
        return ResponseEntity.ok(postService.getPostsOfUserWithNick(pageable,nick,currentUser).map(postDtoConverter::postToGetPostDto));
    }
    

    @GetMapping("/public")
    public ResponseEntity<?> getAllPublic(@PageableDefault(size = 10,page=0) Pageable pageable,
                                           HttpServletRequest request){

        Page<GetPostDto> result = postService.getAllPublic(pageable);

        if (result.isEmpty()) {
            throw new PostNotFoundException("Posts no econtrados");
        } else {

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

            return ResponseEntity.ok().header("link", paginationLinksUtils.createLinkHeader(result, uriBuilder))
                    .body(result);

        }
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
                                             @PathVariable Long id,
                                              @AuthenticationPrincipal Usuario current) throws Exception {

        Optional<Post> postBuscado = postService.findById(id);


        if(postBuscado.isPresent()){
            Post postAEditar = postBuscado.get();
            if(current.getId().equals(postAEditar.getUsuario().getId()))
                return ResponseEntity.status(HttpStatus.OK).body(postDtoConverter.postToGetPostDto(postService.edit(postAEditar,editedPost,file)));
            else
                throw new UnauthorizedRequestException("Esta publicación no pertenece al usuario de la sesión actual.");

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
