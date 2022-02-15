package com.salesianos.dam.model.dto.Post;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePostDto {

    private String titulo;
    private String texto;
    private boolean publica;

}
