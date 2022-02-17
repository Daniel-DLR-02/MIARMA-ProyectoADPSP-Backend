package com.salesianos.dam.errors.model;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.stereotype.Component;


@Component
public class MyDefaultErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Map<String,Object> result =  Map.of(
                "estado",errorAttributes.get("status"),
                "codigo", HttpStatus.valueOf((int) errorAttributes.get("status")).name(),
                "fecha", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                "ruta", errorAttributes.get("path"));

        if (errorAttributes.containsKey("message")) {
            result.put("mensaje", errorAttributes.get("message"));
        }

        if (errorAttributes.containsKey("errors")) {
            result.put("subErrors", errorAttributes.get("errors"));
        }

        return result;
    }




}
