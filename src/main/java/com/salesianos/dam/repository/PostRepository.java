package com.salesianos.dam.repository;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT * FROM post p
            WHERE p.publica = true
           """,nativeQuery = true)
    List<Post> findPostPublic();

}
