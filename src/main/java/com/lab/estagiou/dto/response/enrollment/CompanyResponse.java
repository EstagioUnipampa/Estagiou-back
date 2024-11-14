package com.lab.estagiou.dto.response.enrollment;

import java.util.UUID;

import com.lab.estagiou.model.company.CompanyEntity;

import lombok.Data;

@Data
public class CompanyResponse {

    private UUID id;
    private String name;
    private String email;
    private String cnpj;
    private String accountableName;

    public CompanyResponse(CompanyEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.cnpj = entity.getCnpj();
        this.accountableName = entity.getAccountableName();
    }

}
