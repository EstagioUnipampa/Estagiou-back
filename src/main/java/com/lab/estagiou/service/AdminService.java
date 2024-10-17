package com.lab.estagiou.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.lab.estagiou.dto.request.model.admin.AdminRegisterRequest;
import com.lab.estagiou.exception.generic.EmailAlreadyRegisteredException;
import com.lab.estagiou.exception.generic.NoContentException;
import com.lab.estagiou.exception.generic.NotFoundException;
import com.lab.estagiou.model.admin.AdminEntity;
import com.lab.estagiou.model.admin.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private static final String ADMIN_NOT_FOUND = "Admin not found: ";

    public ResponseEntity<Object> registerAdmin(AdminRegisterRequest request) {

        AdminEntity admin = new AdminEntity(request, true);

        try {
            adminRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyRegisteredException("Email já registrado");
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<AdminEntity>> listAdmins() {
        List<AdminEntity> admins = adminRepository.findAll();

        if (admins.isEmpty()) {
            throw new NoContentException("No admins registered");
        }

        return ResponseEntity.ok(admins);
    }

    public ResponseEntity<Object> searchAdminById(UUID id, Authentication authentication) {
        // super.verifyAuthorization(authentication, id);

        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND + id));

        // log(LogEnum.INFO, "Admin found: " + admin.getId(), HttpStatus.OK.value());
        return ResponseEntity.ok(admin);
    }

    public ResponseEntity<Object> deleteAdminById(UUID id, Authentication authentication) {
        // super.verifyAuthorization(authentication, id);

        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND + id));

        adminRepository.delete(admin);

        // log(LogEnum.INFO, "Admin deleted: " + admin.getId(),
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Object> updateAdmin(UUID id, AdminRegisterRequest requestCadastro,
            Authentication authentication) {
        // super.verifyAuthorization(authentication, id);

        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND + id));

        admin.update(requestCadastro);
        adminRepository.save(admin);

        // log(LogEnum.INFO, "Admin updated: " + admin.getId(),
        // HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }

}
