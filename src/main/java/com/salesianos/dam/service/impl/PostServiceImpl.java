package com.salesianos.dam.service.impl;

import com.salesianos.dam.exception.PostNotFoundException;
import com.salesianos.dam.exception.UnauthorizedRequestException;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
                .usuario(user)
                .ficheroAdjuntoResized(uriResized)
                .publica(createPostDto.isPublica())
                .build();

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
            postAEditar.setFicheroAdjunto(uriOriginal);
            postAEditar.setFicheroAdjuntoResized(uriResized);

        }

        postAEditar.setTexto(postEdited.getTexto());
        postAEditar.setTitulo( postEdited.getTitulo());
        postAEditar.setPublica( postEdited.isPublica());

        repository.save(postAEditar);

        return postAEditar;
    }

    @Override
    public List<GetPostDto> getAllPublic() {

        return repository.findPostPublic().stream().map(dtoConverter::postToGetPostDto).collect(Collectors.toList());
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
                throw new UnauthorizedRequestException("No se puede acceder al post al ser privado.");
            }
        }
        else{
            throw new PostNotFoundException("Post no encontrado");
        }
    }



}
