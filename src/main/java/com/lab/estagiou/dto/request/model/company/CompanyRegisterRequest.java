package com.lab.estagiou.dto.request.model.company;

import org.hibernate.validator.constraints.br.CNPJ;

import com.lab.estagiou.dto.request.model.util.RequestEmailAddressRegister;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyRegisterRequest extends RequestEmailAddressRegister {

    @CNPJ
    private String cnpj;
    private String accountableName;

}
