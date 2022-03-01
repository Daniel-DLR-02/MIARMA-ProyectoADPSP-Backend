package com.salesianos.dam.repository;


import com.salesianos.dam.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            WHERE u.id IN (SELECT user_id FROM follows
                          WHERE following_id = :followed_id)
            """,nativeQuery = true)
    List<Usuario> findFollowers(@Param("followed_id") UUID id);


    @Query(value="""
            SELECT * FROM usuario u
            WHERE u.id IN (SELECT id_seguidor FROM solicitud_seguimiento
                            WHERE usuario_id = :id_user_requested)
            """,nativeQuery = true)
    Page<Usuario> getUsersWithRequestToUser(Pageable pageable, @Param("id_user_requested") UUID id);


    boolean existsByNick(String nick);

    boolean existsByEmail(String email);
}

