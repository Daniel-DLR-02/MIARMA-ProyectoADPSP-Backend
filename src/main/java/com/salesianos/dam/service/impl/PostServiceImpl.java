package com.salesianos.dam.service.impl;

import com.salesianos.dam.errors.exception.PostNotFoundException;
import com.salesianos.dam.errors.exception.StorageException;
import com.salesianos.dam.errors.exception.UnauthorizedRequestException;
import com.salesianos.dam.errors.exception.UserNotFoundException;
import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Post.CreatePostDto;
import com.salesianos.dam.model.dto.Post.GetPostDto;
import com.salesianos.dam.model.dto.Post.PostDtoConverter;
import com.salesianos.dam.repository.PostRepository;
import com.salesianos.dam.repository.UsuarioRepository;
import com.salesianos.dam.service.PostService;
import com.salesianos.dam.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PostDtoConverter dtoConverter;
    private final StorageService storageService;

    @Override
    public Post save(CreatePostDto createPostDto, MultipartFile file,Usuario user) throws Exception {

        String filenameOriginal = storageService.storeOriginal(file);

        String extension = StringUtils.getFilenameExtension(filenameOriginal);
        String filenameResized = "";
        String uriResized = "";
        String uriOriginal = storageService.createUri(filenameOriginal);


        Post newPost = Post.builder()
                .titulo(createPostDto.getTitulo())
                .texto(createPostDto.getTexto())
                .ficheroAdjunto(uriOriginal)
                .usuario(user)
                .publica(createPostDto.isPublica())
                .build();

        if(file.getContentType().matches("image/\\D+")) {
            filenameResized = storageService.storeImageResized(file, 1024);

            uriResized = storageService.createUri(filenameResized);

        }
        else if(file.getContentType().equals("video/mp4")){

            filenameResized = storageService.storeVideoResized(file, 1024);

            uriResized = storageService.createUri(filenameResized);

        }

        newPost.setFicheroAdjuntoResized(uriResized);
        usuarioRepository.save(user);
        return repository.save(newPost);

    }

    @Override
    public void delete(Long id) throws IOException {

        Post post = repository.getById(id);

        post.setUsuario(null);
        Path rutaFichero = storageService.load(StringUtils.cleanPath(String.valueOf(post.getFicheroAdjunto())).replace("http://localhost:8080/download/",""));
        storageService.deleteFile(rutaFichero);
        Path rutaFicheroReescalado = storageService.load(StringUtils.cleanPath(String.valueOf(post.getFicheroAdjuntoResized())).replace("http://localhost:8080/download/",""));
        storageService.deleteFile(rutaFicheroReescalado);

        repository.delete(post);
    }

    @Override
    public List<Post> findAll() {
        return null;
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return repository.findById(postId);
    }

    @Override
    public Post getById(Long postId) {
        return repository.getById(postId);
    }

    @Override
    public Post edit(Post postAEditar,CreatePostDto postEdited,MultipartFile file) throws Exception {

        if(!file.isEmpty()){

            Path rutaFicheroAdjunto = storageService.load(StringUtils.cleanPath(String.valueOf(postAEditar.getFicheroAdjunto())).replace("http://localhost:8080/download/",""));
            storageService.deleteFile(rutaFicheroAdjunto);

            Path rutaFicheroAdjuntoResized = storageService.load(StringUtils.cleanPath(String.valueOf(postAEditar.getFicheroAdjuntoResized())).replace("http://localhost:8080/download/",""));
            storageService.deleteFile(rutaFicheroAdjuntoResized);
            String filenameResized="";
            String uriResized="";
            String filenameOriginal = storageService.storeOriginal(file);

            String uriArchivoOriginal = storageService.createUri(filenameOriginal);

            postAEditar.setFicheroAdjunto(uriArchivoOriginal);

            if(file.getContentType().matches("image/\\D+")) {
                filenameResized = storageService.storeImageResized(file, 1024);

                uriResized = storageService.createUri(filenameResized);

            }
            else if(file.getContentType().equals("video/mp4")){

                filenameResized = storageService.storeVideoResized(file, 1024);

                uriResized = storageService.createUri(filenameResized);

            }
            postAEditar.setFicheroAdjuntoResized(uriResized);


        }

        postAEditar.setTexto(postEdited.getTexto());
        postAEditar.setTitulo( postEdited.getTitulo());
        postAEditar.setPublica( postEdited.isPublica());

        repository.save(postAEditar);

        return postAEditar;
    }


    @Override
    public Page<GetPostDto> getAllPublic(Pageable pageable) {

        return repository.findPostPublic(pageable).map(dtoConverter::postToGetPostDto);
    }

    @Override
    public List<GetPostDto> getUserPosts(UUID id) {

        return repository.findCurrentUserPostsWithId(id).stream().map(dtoConverter::postToGetPostDto).collect(Collectors.toList());

    }

    @Override
    public Post getPostById(Long id, Usuario user) {

        Optional<Post> postBuscado = repository.findById(id);
        if(postBuscado.isPresent()){
            Post postEncontrado = postBuscado.get();
            List<UUID> idSeguidoresPropietarioPublicacion = usuarioRepository.findFollowers(postEncontrado.getUsuario().getId()).stream().map(Usuario::getId).toList();
            if(postEncontrado.getUsuario().getId().equals(user.getId()) ||
                    postEncontrado.isPublica() || idSeguidoresPropietarioPublicacion.contains(user.getId())){
                return postBuscado.get();
            }
            else{
                throw new UnauthorizedRequestException("No se puede acceder a los posts de este usuario al ser privado.");
            }
        }
        else{
            throw new PostNotFoundException("Post no encontrado");
        }
    }

    @Override
    public List<Post> getPostsOfUserWithNick(String nick, Usuario currentUser) {
        Usuario userBuscado = usuarioRepository.findByNick(nick).orElseThrow(()->new UserNotFoundException("El usuario con el nick especificado no existe."));
        List<UUID> idSeguidoresPropietarioCuenta = usuarioRepository.findFollowers(userBuscado.getId()).stream().map(Usuario::getId).toList();
        if(userBuscado.getId().equals(currentUser.getId()) || idSeguidoresPropietarioCuenta.contains(currentUser.getId())){
            return currentUser.getPosts();
        }
        else{
            return repository.getPublicPostsOfUser(userBuscado.getId());
        }

    }


}
