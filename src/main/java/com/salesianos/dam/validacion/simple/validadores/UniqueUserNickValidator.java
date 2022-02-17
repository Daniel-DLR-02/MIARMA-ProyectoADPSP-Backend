package com.salesianos.dam.validacion.simple.validadores;


import com.salesianos.dam.service.UsuarioService;
import com.salesianos.dam.validacion.simple.anotaciones.UniqueUserNick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserNickValidator implements ConstraintValidator<UniqueUserNick, String> {

    @Autowired
    private UsuarioService service;

    @Override
    public void initialize(UniqueUserNick constraintAnnotation) { }

    @Override
    public boolean isValid(String nick, ConstraintValidatorContext context) {
        return StringUtils.hasText(nick) && !service.existsByNick(nick);
    }
}
