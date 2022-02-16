package com.salesianos.dam.repository;


import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository  extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findFirstByNick(String nick);
    Optional<Usuario> findByNick(String nick);

    @Query(value = """
            SELECT * FROM usuario u
            WHERE u.id = (SELECT user_id FROM follows
                          WHERE following_id = :followed_id)
            """,nativeQuery = true)
    List<Usuario> findFollowers(@Param("followed_id") UUID id);



}

