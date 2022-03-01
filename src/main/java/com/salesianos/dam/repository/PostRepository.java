package com.salesianos.dam.repository;

import com.salesianos.dam.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Post> findPostPublic(Pageable pageable);


    @Query(value = """
            SELECT * FROM post p
            WHERE p.usuario_id = :id
           """,nativeQuery = true)
    Page<Post>findCurrentUserPostsWithId(Pageable pageable,@Param("id") UUID id);

    @Query(value = """
            SELECT * FROM post p
            WHERE p.usuario_id = :id
            AND publica = true
            """,nativeQuery = true)
    Page<Post> getPublicPostsOfUser(Pageable pageable,@Param("id") UUID id);

}
