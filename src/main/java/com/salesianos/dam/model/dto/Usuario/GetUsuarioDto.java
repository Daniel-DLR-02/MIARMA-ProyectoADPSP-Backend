package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.model.Post;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class GetUsuarioDto {

    private String nick;
    private String email;
    private String avatar;
    private String resizedAvatar;
    private List<Post> posts;
    private boolean perfilPublico;
}
