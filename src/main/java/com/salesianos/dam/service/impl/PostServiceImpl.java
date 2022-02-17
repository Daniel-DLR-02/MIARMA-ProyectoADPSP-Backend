package com.salesianos.dam.service.impl;

import com.salesianos.dam.errors.exception.PostNotFoundException;
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

        List<String> videoExtension = Arrays.asList("webm","mkv","flv","vob","ogv","ogg",
                "rrc","gifv","mng","mov","avi","qt","wmv","yuv","rm","asf","amv","mp4","m4p","m4v","mpg","mp2","mpeg","mpe",
                "mpv","m4v","svi","3gp","3gpp","3g2","mxf","roq","nsv","flv","f4v","f4p","f4a","f4b","mod");

        String extension = StringUtils.getFilenameExtension(StringUtils.cleanPath(file.getOriginalFilename()));

        String filenameOriginal = storageService.storeOriginal(file);

        String uriOriginal = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filenameOriginal)
                .toUriString();


        Post newPost = Post.builder()
                .titulo(createPostDto.getTitulo())
                .texto(createPostDto.getTexto())
                .ficheroAdjunto(uriOriginal)
                .usuario(user)
                .publica(createPostDto.isPublica())
                .build();

        if(!videoExtension.contains(extension)) {
            String filenameResized = storageService.storeImageResized(file, 1024);

            String uriResized = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(filenameResized)
                    .toUriString();

            newPost.setFicheroAdjuntoResized(uriResized);
        }
        else{

            String filenameResized = storageService.storeVideoResized(file, 1024);

            String uriResized = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(filenameResized)
                    .toUriString();

            newPost.setFicheroAdjuntoResized(uriResized);
        }

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


            String filenameOriginal = storageService.storeOriginal(file);

            String uriOriginal = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(filenameOriginal)
                    .toUriString();

            String filenameResized = storageService.storeImageResized(file,1024);

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
