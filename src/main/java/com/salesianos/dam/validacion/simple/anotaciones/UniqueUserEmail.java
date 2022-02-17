package com.salesianos.dam.validacion.simple.anotaciones;

import com.salesianos.dam.validacion.simple.validadores.UniqueUserEmailValidator;
import com.salesianos.dam.validacion.simple.validadores.UniqueUserNickValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserEmailValidator.class)
@Documented
public @interface UniqueUserEmail {
    String message() default "No puede haber dos usuarios con el mismo email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
