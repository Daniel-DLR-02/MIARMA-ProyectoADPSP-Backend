package com.salesianos.dam.service;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Post.CreatePostDto;
import com.salesianos.dam.model.dto.Post.GetPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {

    Post save(CreatePostDto createPostDto, MultipartFile file, Usuario user) throws Exception;

    void delete(Long id) throws IOException;

    List<Post> findAll();

    Optional<Post> findById(Long postId);

    Post getById(Long postId);

    Post edit(Post postAEditar,CreatePostDto postEdited,MultipartFile file) throws Exception;

    Page<GetPostDto> getAllPublic(Pageable pageable);

    Page<GetPostDto> getUserPosts(Pageable pageable,UUID id);

    Post getPostById(Long id, Usuario user);

    Page<Post>  getPostsOfUserWithNick(Pageable pageable,String nick,Usuario currentUser);
}
