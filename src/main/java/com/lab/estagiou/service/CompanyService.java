package com.lab.estagiou.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import com.lab.estagiou.dto.request.model.company.CompanyRegisterRequest;
import com.lab.estagiou.exception.generic.EmailAlreadyRegisteredException;
import com.lab.estagiou.exception.generic.NoContentException;
import com.lab.estagiou.exception.generic.NotFoundException;
import com.lab.estagiou.model.company.CompanyEntity;
import com.lab.estagiou.model.company.CompanyRepository;
import com.lab.estagiou.model.company.exception.CnpjAlreadyRegisteredException;
import com.lab.estagiou.model.emailconfirmationtoken.EmailConfirmationTokenEntity;
import com.lab.estagiou.model.emailconfirmationtoken.EmailConfirmationTokenRepository;
import com.lab.estagiou.model.log.LogEnum;
import com.lab.estagiou.model.user.UserEntity;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmailSendService emailService;

    @Autowired
    private EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    private static final String COMPANY_NOT_FOUND = "Company not found: ";

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);

    @Value("${spring.mail.enable}")
    private boolean mailInviteEnabled;

    public ResponseEntity<Object> registerCompany(CompanyRegisterRequest request) {
        validateUserAndCompany(request);

        UserEntity company = new CompanyEntity(request);

        if (!mailInviteEnabled) {
            company.setEnabled(true);
        }

        // userRepository.save(company);

        if (mailInviteEnabled) {
            createConfirmationEmailAndSend(company);
        }

        // log(LogEnum.INFO, "Registered company: " + company.getId(),
        // HttpStatus.OK.value());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<CompanyEntity>> listCompanies() {
        List<CompanyEntity> companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            throw new NoContentException("No companies registered");
        }

        // log(LogEnum.INFO, "List companies: " + companies.size() + " companies",
        // HttpStatus.OK.value());
        return ResponseEntity.ok(companies);
    }

    public ResponseEntity<CompanyEntity> searchCompanyById(UUID id) {
        CompanyEntity company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMPANY_NOT_FOUND + id));

        // log(LogEnum.INFO, "Company found: " + company.getEmail(),
        // HttpStatus.OK.value());
        return ResponseEntity.ok(company);
    }

    public ResponseEntity<Object> deleteCompanyById(UUID id, Authentication authentication) {
        // verifyAuthorization(authentication, id);

        if (!companyRepository.existsById(id)) {
            throw new NotFoundException(COMPANY_NOT_FOUND + id);
        }

        companyRepository.deleteById(id);

        // log(LogEnum.INFO, "Company deleted: " + id, HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Object> updateCompany(UUID id, CompanyRegisterRequest request,
            Authentication authentication) {
        // verifyAuthorization(authentication, id);

        CompanyEntity company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMPANY_NOT_FOUND + id));

        company.update(request);
        companyRepository.save(company);

        // log(LogEnum.INFO, "Company updated: " + company.getId(),
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

    private void validateUserAndCompany(CompanyRegisterRequest request) {
        // if (userExists(request)) {
        // throw new EmailAlreadyRegisteredException("Email já cadastrado: " +
        // request.getEmail());
        // }

        if (companyRepository.existsByCnpj(request.getCnpj())) {
            throw new CnpjAlreadyRegisteredException("CNPJ já cadastrado: " + request.getCnpj());
        }
    }

    private ResponseEntity<Object> createConfirmationEmailAndSend(UserEntity user) {
        EmailConfirmationTokenEntity email = createConfirmationEmail(user);
        emailService.sendEmailAsync(email);
        return ResponseEntity.ok().build();
    }

    private EmailConfirmationTokenEntity createConfirmationEmail(UserEntity user) {
        String token = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()),
                StandardCharsets.US_ASCII);
        EmailConfirmationTokenEntity emailConfirmationToken = new EmailConfirmationTokenEntity(token, user);

        return emailConfirmationTokenRepository.save(emailConfirmationToken);
    }
}