package com.salesianos.dam.repository;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT * FROM post p
            WHERE p.publica = true
           """,nativeQuery = true)
    List<Post> findPostPublic();


    @Query(value = """
            SELECT * FROM post p
            WHERE p.usuario_id = :id
           """,nativeQuery = true)
    List<Post>findCurrentUserPostsWithId(@Param("id") UUID id);

    @Query(value = """
            SELECT * FROM post p
            WHERE p.usuario_id = :id
            AND publica = true
            """,nativeQuery = true)
    List<Post> getPublicPostsOfUser(@Param("id") UUID id);

}
