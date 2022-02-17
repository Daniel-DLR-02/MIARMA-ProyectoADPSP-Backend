package com.salesianos.dam.model.dto.Post;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreatePostDto {

    private String titulo;
    private String texto;
    private boolean publica;

}
