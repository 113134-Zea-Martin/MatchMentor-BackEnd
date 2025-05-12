package com.scaffold.template.dtos.mercadoPagoAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateTokenDTO {

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("client_id")
    private String clientID;

    @JsonProperty("grant_type")
    private String grantType;
    private String code;

    @JsonProperty("code_verifier")
    private String codeVerifier;

    @JsonProperty("redirect_uri")
    private String redirectURI;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("test_token")
    private boolean testToken;
}
