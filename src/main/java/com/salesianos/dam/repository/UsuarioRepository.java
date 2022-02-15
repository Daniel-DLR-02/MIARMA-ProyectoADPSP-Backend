package com.salesianos.dam.repository;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository  extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByNick(String nick);

    @EntityGraph("grafo-usuario-follower")
    List<Usuario> findByIdNotNull();


}

