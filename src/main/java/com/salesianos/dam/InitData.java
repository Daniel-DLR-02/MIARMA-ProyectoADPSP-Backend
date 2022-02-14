package com.salesianos.dam;


import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class InitData {

    private final UsuarioService usuarioService;

    @PostConstruct
    public void data(){
        //Usuario userProp1= usuarioService.save(CreateUsuarioDto.builder().nombre("Antonio").nick("Antnio").email("antonio@gmail.com").fechaNacimiento(LocalDate.of(2002,03,03)).password1("asdfg").password2("asdfg").build());

    }
}
