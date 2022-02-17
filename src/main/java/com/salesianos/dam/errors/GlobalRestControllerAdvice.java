package com.salesianos.dam.errors;


import com.salesianos.dam.errors.exception.*;
import com.salesianos.dam.errors.model.ApiError;
import com.salesianos.dam.errors.model.ApiSubError;
import com.salesianos.dam.errors.model.ApiValidationSubError;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return buildApiError404(ex, request);
    }

    @ExceptionHandler({FileNotFoundException.class})
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        return buildApiError404(ex, request);
    }

    @ExceptionHandler({PostNotFoundException.class})
    public ResponseEntity<?> handlePostNotFoundException(PostNotFoundException ex, WebRequest request) {
        return buildApiError404(ex, request);
    }

    @ExceptionHandler({RequestNotFoundException.class})
    public ResponseEntity<?> handleRequestNotFoundException(RequestNotFoundException ex, WebRequest request) {
        return buildApiError404(ex, request);
    }

    @ExceptionHandler({StorageException.class})
    public ResponseEntity<?> handleStorageException(StorageException ex, WebRequest request) {
        return buildApiError400(ex, request);
    }

    @ExceptionHandler({UnauthorizedRequestException.class})
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedRequestException ex, WebRequest request) {
        return buildApiError403(ex, request);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return buildApiErrorWithSubError(HttpStatus.BAD_REQUEST,
                "Errores varios en la validación",
                request,
                ex.getConstraintViolations()
                        .stream()
                        .map(cv -> ApiValidationSubError.builder()
                                .objeto(cv.getRootBeanClass().getSimpleName())
                                .campo(((PathImpl)cv.getPropertyPath()).getLeafNode().asString())
                                .valorRechazado(cv.getInvalidValue())
                                .mensaje(cv.getMessage())
                                .build())
                        .collect(Collectors.toList())
        );

    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {


        List<ApiSubError> subErrorList = new ArrayList<>();

        ex.getAllErrors().forEach(error -> {

            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;

                subErrorList.add(
                        ApiValidationSubError.builder()
                                .objeto(fieldError.getObjectName())
                                .campo(fieldError.getField())
                                .valorRechazado(fieldError.getRejectedValue())
                                .mensaje(fieldError.getDefaultMessage())
                                .build()
                );
            }
            else
            {
                ObjectError objectError = (ObjectError) error;

                subErrorList.add(
                        ApiValidationSubError.builder()
                                .objeto(objectError.getObjectName())
                                .mensaje(objectError.getDefaultMessage())
                                .build()
                );
            }


        });



        return buildApiErrorWithSubError(HttpStatus.BAD_REQUEST, "Errores varios en la validación",
                request,
                subErrorList.isEmpty() ? null : subErrorList
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildApiErrorStatus(status, ex, request);
    }


    private ResponseEntity<Object> buildApiError400(Exception ex, WebRequest request) {
        return buildApiErrorStatus(HttpStatus.BAD_REQUEST, ex, request);
    }


    private ResponseEntity<Object> buildApiError404(Exception ex, WebRequest request) {
        return buildApiErrorStatus(HttpStatus.NOT_FOUND, ex, request);
    }

    private ResponseEntity<Object> buildApiError403(Exception ex, WebRequest request) {
        return buildApiErrorStatus(HttpStatus.FORBIDDEN, ex, request);
    }

    private ResponseEntity<Object> buildApiErrorStatus(HttpStatus status, Exception ex, WebRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(status, ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI()));
    }

    private ResponseEntity<Object> buildApiErrorWithSubError(HttpStatus estado, String mensaje, WebRequest request, List<ApiSubError> subErrores) {
        return ResponseEntity
                .status(estado)
                .body(new ApiError(estado, mensaje, ((ServletWebRequest) request).getRequest().getRequestURI(), subErrores));

    }


}