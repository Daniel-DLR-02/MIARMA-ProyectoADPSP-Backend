package com.salesianos.dam.validacion.simple.validadores;

import com.salesianos.dam.service.UsuarioService;
import com.salesianos.dam.validacion.simple.anotaciones.UniqueUserEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String>{
    @Autowired
    private UsuarioService service;

    @Override
    public void initialize(UniqueUserEmail constraintAnnotation) { }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return StringUtils.hasText(email) && !service.existsByEmail(email);
    }
}
