package com.scaffold.template.controllers;

import com.scaffold.template.dtos.mercadoPagoAuth.CreateMercadoPagoAuthDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenResponseDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.GetTokenRequestDTO;
import com.scaffold.template.services.mercadoPagoAuth.MercadoPagoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mercadoPagoAuth")
public class MercadoPagoAuthController {

    private final MercadoPagoAuthService mercadoPagoAuthService;

    @Autowired
    public MercadoPagoAuthController(MercadoPagoAuthService mercadoPagoAuthService) {
        this.mercadoPagoAuthService = mercadoPagoAuthService;
    }

    @PostMapping("/create")
    public String create(@RequestBody CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO) {
        return mercadoPagoAuthService.getURL(createMercadoPagoAuthDTO);
    }

    @PostMapping()
    public CreateTokenResponseDTO createToken(@RequestBody GetTokenRequestDTO getTokenRequestDTO) {
        return mercadoPagoAuthService.createToken(getTokenRequestDTO);
    }
}
