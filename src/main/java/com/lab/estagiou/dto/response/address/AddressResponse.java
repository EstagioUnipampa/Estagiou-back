package com.lab.estagiou.dto.response.address;

import com.lab.estagiou.model.address.AddressEntity;

import lombok.Data;

@Data
public class AddressResponse {

    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String complement;

    public AddressResponse(AddressEntity entity) {
        this.country = entity.getCountry();
        this.state = entity.getState();
        this.city = entity.getCity();
        this.neighborhood = entity.getNeighborhood();
        this.street = entity.getStreet();
        this.number = entity.getNumber();
        this.complement = entity.getComplement();
    }

}
