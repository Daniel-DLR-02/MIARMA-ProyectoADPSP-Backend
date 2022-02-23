package com.salesianos.dam.service.impl;

import com.salesianos.dam.errors.exception.RequestNotFoundException;
import com.salesianos.dam.errors.exception.UnauthorizedRequestException;
import com.salesianos.dam.errors.exception.UserNotFoundException;
import com.salesianos.dam.model.SolicitudSeguimiento;
import com.salesianos.dam.model.UserRole;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Peticion.GetPeticionDto;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.UsuarioDtoConverter;
import com.salesianos.dam.repository.SolicitudSeguimientoRepository;
import com.salesianos.dam.repository.UsuarioRepository;
import com.salesianos.dam.service.StorageService;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository repository;
    private final StorageService storageService;
    private final UsuarioDtoConverter dtoConverter;
    private final PasswordEncoder passwordEncoder;
    private final SolicitudSeguimientoRepository requestRepos;

    @Override
    public Usuario save(CreateUsuarioDto createUsuarioDto, MultipartFile file) throws Exception {

        String filenameResized = storageService.storeImageResized(file,128);


        String uri = storageService.createUri(filenameResized);

        return repository.save(Usuario.builder()
                .nombre(createUsuarioDto.getNombre())
                .nick(createUsuarioDto.getNick())
                .email(createUsuarioDto.getEmail())
                .fechaNacimiento(createUsuarioDto.getFechaNacimiento())
                .password(passwordEncoder.encode(createUsuarioDto.getPassword()))
                .perfilPublico(createUsuarioDto.isPublico())
                .avatar(uri)
                .role(UserRole.USER)
                .build());


    }

    @Override
    public Usuario edit(Usuario usuarioAEditar, CreateUsuarioDto usuarioEditado, MultipartFile file) throws Exception {

        if(!file.isEmpty()){

            Path rutaFichero = storageService.load(StringUtils.cleanPath(String.valueOf(usuarioAEditar.getAvatar())).replace("http://localhost:8080/download/",""));
            storageService.deleteFile(rutaFichero);

            String filenameResized = storageService.storeImageResized(file,128);

            String uriResized = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(filenameResized)
                    .toUriString();

            usuarioAEditar.setAvatar(uriResized);

        }

        usuarioAEditar.setNombre(usuarioEditado.getNombre());
        usuarioAEditar.setNick(usuarioEditado.getNick());
        usuarioAEditar.setEmail(usuarioEditado.getEmail());
        usuarioAEditar.setFechaNacimiento(usuarioAEditar.getFechaNacimiento());
        usuarioAEditar.setFechaNacimiento(usuarioEditado.getFechaNacimiento());
        usuarioAEditar.setPassword(usuarioEditado.getPassword());

        repository.save(usuarioAEditar);

        return usuarioAEditar;
    }

    @Override
    public GetUsuarioDto getUsuarioById(UUID id, Usuario currentUser) {

        Optional<Usuario> usuarioAMostrar = repository.findById(id);

        if(usuarioAMostrar.isPresent()){
            if(currentUser.getFollows().contains(usuarioAMostrar.get()) || usuarioAMostrar.get().isPerfilPublico()){
                return dtoConverter.usuarioToGetUsuarioDto(usuarioAMostrar.get());
            }
            else{
                throw new UnauthorizedRequestException("El perfil es privado.");
            }
        }
        else{
            throw new UserNotFoundException("Usuario búscado no encontrado.");
        }
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Usuario> findById(UUID userId) {
        return repository.findById(userId);
    }

    @Override
    public GetPeticionDto createFollowRequest(Usuario currentUser, String nick) {

        Optional<Usuario> usuarioReclamado = repository.findByNick(nick);

        if(usuarioReclamado.isPresent()) {
            if(!(usuarioReclamado.get().getId().equals(currentUser.getId())||currentUser.getFollows().contains(usuarioReclamado.get()))){
                SolicitudSeguimiento followRequest = SolicitudSeguimiento.builder()
                        .idSeguidor(currentUser.getId())
                        .usuario(usuarioReclamado.get())
                        .build();
                requestRepos.save(followRequest);
                repository.save(currentUser);
                return GetPeticionDto.builder()
                        .idSolicitud(followRequest.getId())
                        .idSeguidor(currentUser.getId())
                        .idSeguido(usuarioReclamado.get().getId())
                        .build();
            }
            else{
                throw new UnauthorizedRequestException("Un usuario no puede seguirse así mismo ni a un usuario que ya sigue.");
            }
        }
        else{
            throw new UserNotFoundException("Usuario reclamado no encontrado.");
        }
    }

    @Override
    public void acceptFollowRequest(Usuario currentUser, Long idRequest) {
        Optional<SolicitudSeguimiento> solicitudSeguimiento = requestRepos.findById(idRequest);
        if(solicitudSeguimiento.isPresent()){
            if(solicitudSeguimiento.get().getUsuario().getId().equals(currentUser.getId())){
                repository.getById(solicitudSeguimiento.get().getIdSeguidor()).getFollows().add(currentUser);
                requestRepos.deleteById(idRequest);
                repository.save(currentUser);
            }else{
                throw new UnauthorizedRequestException("Esta petición de seguimiento no pertenece al perfil logueado.") ;
            }
        }
        else{
            throw new RequestNotFoundException("Petición de seguimiento no encontrada");
        }
    }

    @Override
    public void declineFollowRequest(Usuario currentUser, Long idRequest) {
        Optional<SolicitudSeguimiento> solicitudSeguimiento = requestRepos.findById(idRequest);
        if(solicitudSeguimiento.isPresent()){
            if(solicitudSeguimiento.get().getUsuario().getId().equals(currentUser.getId())){
                requestRepos.deleteById(idRequest);
            }else{
                throw new UnauthorizedRequestException("Esta petición de seguimiento no pertenece al perfil logueado.") ;
            }
        }
        else{
            throw new RequestNotFoundException("Petición de seguimiento no encontrada");
        }
    }

    @Override
    public Page<GetUsuarioDto> peticionesDelUsuarioActual(Pageable pageable, Usuario currentUser) {
        Page<GetUsuarioDto> pagina = repository.getUsersWithRequestToUser(pageable,currentUser.getId()).map(dtoConverter::usuarioToGetUsuarioDto);
        if(pagina.getContent().isEmpty()){
            throw new RequestNotFoundException("No hay peticiones.");
        }
        else{
            return pagina;
        }
    }

    @Override
    public boolean existsByNick(String nick) {
        return repository.existsByNick(nick);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repository.findFirstByNick(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick+ "no encontrado"));
    }
}
