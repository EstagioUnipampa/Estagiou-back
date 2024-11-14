package com.lab.estagiou.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lab.estagiou.exception.generic.EmailAlreadyRegisteredException;
import com.lab.estagiou.exception.generic.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

        @ExceptionHandler({ EmailAlreadyRegisteredException.class })
        public ResponseEntity<ErrorMessage> usernameUniqueViolationIntegrity(RuntimeException e,
                        HttpServletRequest request) {
                log.error("Api error - {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.CONFLICT, e.getMessage()));
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorMessage> entityNotFound(RuntimeException e,
                        HttpServletRequest request) {
                log.error("Api error - {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, e.getMessage()));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
                log.error("Api error - {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, "Acesso negado"));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessage> internalServerErrorException(Exception e, HttpServletRequest request) {
                ErrorMessage error = new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                log.error("Internal Server Error {} {}", error, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(error);
        }

}
