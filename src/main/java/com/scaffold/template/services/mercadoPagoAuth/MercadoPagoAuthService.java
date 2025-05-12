package com.scaffold.template.services.mercadoPagoAuth;

import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateMercadoPagoAuthDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenResponseDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.GetTokenRequestDTO;
import com.scaffold.template.entities.MercadoPagoAuthEntity;
import org.springframework.stereotype.Service;

@Service
public interface MercadoPagoAuthService {
    /**
     * This method is used to create a new MercadoPagoAuthEntity.
     *
     * @param createMercadoPagoAuthDTO The entity to be created.
     * @return The created entity.
     */
    MercadoPagoAuthEntity create(CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO);
    /**
     * This method is used to create a new MercadoPagoAuthEntity and return its ID.
     *
     * @param createMercadoPagoAuthDTO The entity to be created.
     * @return The ID of the created entity.
     */
    Long createAndGetId(CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO);
    /**
     * This method is used to get the URL for MercadoPago authorization.
     *
     * @param createMercadoPagoAuthDTO The entity to be created.
     * @return The URL for MercadoPago authorization.
     */
    String getURL(CreateMercadoPagoAuthDTO createMercadoPagoAuthDTO);

    /**
        * This method is used to create a new token for MercadoPago.
        *
        * @param getTokenRequestDTO The request object containing the code and user ID.
        * @return The response object containing the token information.
        */
    CreateTokenResponseDTO createToken(GetTokenRequestDTO getTokenRequestDTO);
}
