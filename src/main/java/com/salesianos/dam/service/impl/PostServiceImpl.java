package com.salesianos.dam.service.impl;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.UserRole;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Post.CreatePostDto;
import com.salesianos.dam.repository.PostRepository;
import com.salesianos.dam.repository.UsuarioRepository;
import com.salesianos.dam.service.PostService;
import com.salesianos.dam.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final StorageService storageService;
    private final UsuarioRepository userRepos;

    @Override
    public Post save(CreatePostDto createPostDto, MultipartFile file,Usuario user) throws Exception {

        String filenameOriginal = storageService.storeOriginal(file);

        String uriOriginal = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filenameOriginal)
                .toUriString();

        String filenameResized = storageService.storeResized(file,1024);

        String uriResized = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filenameResized)
                .toUriString();

        Post newPost = Post.builder()
                .titulo(createPostDto.getTitulo())
                .texto(createPostDto.getTexto())
                .ficheroAdjunto(uriOriginal)
                .ficheroAdjuntoResized(uriResized)
                .publica(createPostDto.isPublica())
                .build();
        user.addPost(newPost);
        userRepos.save(user);
        return repository.save(newPost);

    }

    @Override
    public List<Post> findAll() {
        return null;
    }

    @Override
    public Optional<Post> findById(UUID postId) {
        return Optional.empty();
    }
}
