package com.salesianos.dam.repository;

import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository  extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByNick(String nick);

}

