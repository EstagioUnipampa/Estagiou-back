package com.lab.estagiou.controller;

import java.util.UUID;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lab.estagiou.controller.util.UtilController;
import com.lab.estagiou.dto.request.auth.RequestAuthentication;
import com.lab.estagiou.dto.response.auth.LoginResponse;
import com.lab.estagiou.dto.response.error.ErrorResponse;
import com.lab.estagiou.jwt.JwtToken;
import com.lab.estagiou.jwt.JwtUserDetailsService;
import com.lab.estagiou.model.user.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(value = UtilController.API_VERSION + "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Auth", description = "API for management of authentication")
public class AuthController {

    @Autowired
    private JwtUserDetailsService detailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Autenticar na API", description = "Recurso de autenticação na API", responses = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso e retorno de um bearer token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Campo(s) Inválido(s)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody @Valid RequestAuthentication dto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    dto.getEmail(), dto.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getEmail());

            UUID id = userRepository.findByEmail(dto.getEmail()).getId();

            LoginResponse response = new LoginResponse(token.getToken(), id);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Credenciais inválidas", request));
    }

}
