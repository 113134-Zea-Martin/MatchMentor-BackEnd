package com.scaffold.template.dtos.mercadoPagoAuth;

import lombok.Data;

@Data
public class GetTokenRequestDTO {

    private String code;
    private Long userId;

}
