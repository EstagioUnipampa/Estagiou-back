package com.lab.estagiou.dto.response.company;

import java.util.UUID;

import com.lab.estagiou.dto.response.address.AddressResponse;
import com.lab.estagiou.dto.response.job_vacancy.JobVacancyResponse;
import com.lab.estagiou.model.company.CompanyEntity;

import lombok.Data;

@Data
public class CompanyResponse {

    private UUID id;
    private String name;
    private String email;
    private String role;
    private String cnpj;
    private String accountableName;
    private JobVacancyResponse[] jobVacancies;
    private AddressResponse address;

    public CompanyResponse(CompanyEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.role = entity.getRole().toString();
        this.cnpj = entity.getCnpj();
        this.accountableName = entity.getAccountableName();
        this.jobVacancies = entity.getJobVacancies().stream().map(JobVacancyResponse::new)
                .toArray(JobVacancyResponse[]::new);
        this.address = new AddressResponse(entity.getAddress());
    }

}
