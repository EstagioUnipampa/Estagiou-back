package com.lab.estagiou.exception.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.lab.estagiou.dto.response.error.ErrorResponse;
import com.lab.estagiou.exception.handler.util.HandlerExceptionUtil;
import com.lab.estagiou.model.log.LogEnum;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MaxUploadSizeExceededExceptionHandler extends HandlerExceptionUtil {

    @Value("${spring.servlet.multipart.max-request-size}")
    private String fileMaxSize;

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleNotFoundException(Exception e, HttpServletRequest request) {
        log(LogEnum.WARN, e.getClass().getSimpleName() + ": " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), request);
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Limite máximo do tamanho do arquivo excedido (" + fileMaxSize + ").", request);
        return ResponseEntity.badRequest().body(response);
    }
    
}
