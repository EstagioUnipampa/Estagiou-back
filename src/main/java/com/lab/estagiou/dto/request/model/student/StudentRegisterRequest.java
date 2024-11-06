package com.lab.estagiou.dto.request.model.student;

import java.util.List;
import java.util.UUID;

import com.lab.estagiou.dto.request.model.util.RequestEmailAddressRegister;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentRegisterRequest extends RequestEmailAddressRegister {

    private String lastName;
    private UUID course;

    @Size(max = 5)
    private List<UUID> skills;

}
