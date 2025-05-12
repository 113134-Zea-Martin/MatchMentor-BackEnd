package com.scaffold.template.dtos.mercadoPagoAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateTokenResponseDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private long expiresIn;
    
    private String scope;

    @JsonProperty("user_id")
    private long userID;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("public_key")
    private String publicKey;

    @JsonProperty("live_mode")
    private boolean liveMode;
}
