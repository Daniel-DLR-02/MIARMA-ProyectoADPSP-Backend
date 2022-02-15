package com.salesianos.dam.repository;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


}
