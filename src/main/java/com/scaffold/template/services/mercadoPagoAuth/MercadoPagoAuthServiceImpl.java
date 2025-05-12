package com.scaffold.template.services.mercadoPagoAuth;

import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateMercadoPagoAuthDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenResponseDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.GetTokenRequestDTO;
import com.scaffold.template.entities.MercadoPagoAuthEntity;
import com.scaffold.template.repositories.MercadoPagoAuthRepository;
import com.scaffold.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class MercadoPagoAuthServiceImpl implements MercadoPagoAuthService {

    private final MercadoPagoAuthRepository mercadoPagoAuthRepository;
    private final RestTemplate restTemplate;
    private final UserService userService;

    @Value("${mercadopago.application.number}")
    private String applicationNumber;

    @Value("${mercadopago.redirect.url.origin}")
    private String redirectUri;

    @Value("${mercadopago.client.id}")
    private String clientId;

    @Value("${mercadopago.client.secret}")
    private String clientSecret;

    private String URL = "https://auth.mercadopago.com/authorization?" +
            "client_id=APP_ID&" +
            "response_type=code&" +
            "platform_id=mp&" +
            "state=RANDOM_ID&" +
            "redirect_uri=https://www.redirect-url.com";

    @Autowired
    public MercadoPagoAuthServiceImpl(MercadoPagoAuthRepository mercadoPagoAuthRepository,
                                      RestTemplate restTemplate,
                                      UserService userService) {
        this.mercadoPagoAuthRepository = mercadoPagoAuthRepository;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }


    @Override
    public MercadoPagoAuthEntity create(CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO) {
        MercadoPagoAuthEntity mercadoPagoAuthEntity = new MercadoPagoAuthEntity();
        mercadoPagoAuthEntity.setUserId(createMercadoPagoAuthDTO.getUserId());
        mercadoPagoAuthEntity.setCreatedAt(LocalDateTime.now());
        return mercadoPagoAuthRepository.save(mercadoPagoAuthEntity);
    }

    @Override
    public Long createAndGetId(CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO) {
        MercadoPagoAuthEntity mercadoPagoAuth = this.create(createMercadoPagoAuthDTO);
        return mercadoPagoAuth.getId();
    }

    @Override
    public String getURL(CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO) {
        Long randomId = this.createAndGetId(createMercadoPagoAuthDTO);
        return URL
                .replace("APP_ID", applicationNumber)
                .replace("RANDOM_ID", randomId.toString())
                .replace("https://www.redirect-url.com", redirectUri + "/edit-profile");
    }

    @Override
    public CreateTokenResponseDTO createToken(GetTokenRequestDTO getTokenRequestDTO) {
        String url = "https://api.mercadopago.com/oauth/token";
        CreateTokenDTO createTokenDTO = new CreateTokenDTO();
        createTokenDTO.setClientID(clientId);
        createTokenDTO.setClientSecret(clientSecret);
        createTokenDTO.setGrantType("authorization_code");
        createTokenDTO.setCode(getTokenRequestDTO.getCode());
        createTokenDTO.setRedirectURI(redirectUri + "/edit-profile");
        createTokenDTO.setTestToken(false);

        CreateTokenResponseDTO createTokenResponseDTO = restTemplate
                .postForObject(url, createTokenDTO, CreateTokenResponseDTO.class);

        // Set the user ID in the request body
        if (createTokenResponseDTO == null) {
            throw new RuntimeException("Failed to create token");
        }
        userService.setMercadoPagoToken(getTokenRequestDTO.getUserId(), createTokenResponseDTO.getAccessToken());

        return createTokenResponseDTO;
    }

}
