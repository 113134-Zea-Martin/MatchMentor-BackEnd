package com.scaffold.template.services.mercadoPagoAuth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import com.scaffold.template.services.mercadoPagoAuth.MercadoPagoAuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.scaffold.template.dtos.mercadoPagoAuth.CreateMercadoPagoAuthDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.CreateTokenResponseDTO;
import com.scaffold.template.dtos.mercadoPagoAuth.GetTokenRequestDTO;
import com.scaffold.template.entities.MercadoPagoAuthEntity;
import com.scaffold.template.repositories.MercadoPagoAuthRepository;
import com.scaffold.template.services.UserService;

class MercadoPagoAuthServiceImplTest {

    @Mock
    private MercadoPagoAuthRepository mercadoPagoAuthRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    @InjectMocks
    private MercadoPagoAuthServiceImpl mercadoPagoAuthService;

    @Captor
    private ArgumentCaptor<MercadoPagoAuthEntity> mercadoPagoAuthCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(mercadoPagoAuthService, "applicationNumber", "APP_123");
        ReflectionTestUtils.setField(mercadoPagoAuthService, "redirectUri", "https://example.com");
        ReflectionTestUtils.setField(mercadoPagoAuthService, "clientId", "client123");
        ReflectionTestUtils.setField(mercadoPagoAuthService, "clientSecret", "secret123");
        ReflectionTestUtils.setField(mercadoPagoAuthService, "URL", "https://auth.mercadopago.com/authorization?client_id=APP_ID&response_type=code&platform_id=mp&state=RANDOM_ID&redirect_uri=https://www.redirect-url.com");
    }

    @Test
    void createStoresMercadoPagoAuthEntityWithCorrectUserId() {
        CreateMercadoPagoAuthDTO dto = new CreateMercadoPagoAuthDTO();
        dto.setUserId(123L);

        MercadoPagoAuthEntity savedEntity = new MercadoPagoAuthEntity();
        savedEntity.setId(1L);
        savedEntity.setUserId(123L);

        when(mercadoPagoAuthRepository.save(any(MercadoPagoAuthEntity.class))).thenReturn(savedEntity);

        MercadoPagoAuthEntity result = mercadoPagoAuthService.create(dto);

        verify(mercadoPagoAuthRepository).save(mercadoPagoAuthCaptor.capture());
        assertEquals(123L, mercadoPagoAuthCaptor.getValue().getUserId());
        assertNotNull(mercadoPagoAuthCaptor.getValue().getCreatedAt());
        assertEquals(1L, result.getId());
    }

    @Test
    void createAndGetIdReturnsCorrectId() {
        CreateMercadoPagoAuthDTO dto = new CreateMercadoPagoAuthDTO();
        dto.setUserId(123L);

        MercadoPagoAuthEntity savedEntity = new MercadoPagoAuthEntity();
        savedEntity.setId(1L);
        savedEntity.setUserId(123L);

        when(mercadoPagoAuthRepository.save(any(MercadoPagoAuthEntity.class))).thenReturn(savedEntity);

        Long id = mercadoPagoAuthService.createAndGetId(dto);

        assertEquals(1L, id);
    }

    @Test
    void getURLReturnsFormattedURLWithReplacedValues() {
        CreateMercadoPagoAuthDTO dto = new CreateMercadoPagoAuthDTO();
        dto.setUserId(123L);

        MercadoPagoAuthEntity savedEntity = new MercadoPagoAuthEntity();
        savedEntity.setId(999L);
        savedEntity.setUserId(123L);

        when(mercadoPagoAuthRepository.save(any(MercadoPagoAuthEntity.class))).thenReturn(savedEntity);

        String url = mercadoPagoAuthService.getURL(dto);

        assertTrue(url.contains("client_id=APP_123"));
        assertTrue(url.contains("state=999"));
        assertTrue(url.contains("https://example.com/edit-profile"));
    }

    @Test
    void createTokenMakesCorrectRestCallAndUpdatesUser() {
        GetTokenRequestDTO requestDTO = new GetTokenRequestDTO();
        requestDTO.setCode("auth_code_123");
        requestDTO.setUserId(123L);

        CreateTokenResponseDTO responseDTO = new CreateTokenResponseDTO();
        responseDTO.setAccessToken("MP_TOKEN_123");

        when(restTemplate.postForObject(eq("https://api.mercadopago.com/oauth/token"), any(CreateTokenDTO.class), eq(CreateTokenResponseDTO.class)))
                .thenReturn(responseDTO);

        CreateTokenResponseDTO result = mercadoPagoAuthService.createToken(requestDTO);

        verify(userService).setMercadoPagoToken(123L, "MP_TOKEN_123");
        assertEquals("MP_TOKEN_123", result.getAccessToken());
    }

    @Test
    void createTokenThrowsExceptionWhenResponseIsNull() {
        GetTokenRequestDTO requestDTO = new GetTokenRequestDTO();
        requestDTO.setCode("auth_code_123");
        requestDTO.setUserId(123L);

        when(restTemplate.postForObject(eq("https://api.mercadopago.com/oauth/token"), any(CreateTokenDTO.class), eq(CreateTokenResponseDTO.class)))
                .thenReturn(null);

        assertThrows(RuntimeException.class, () -> mercadoPagoAuthService.createToken(requestDTO));
        verify(userService, never()).setMercadoPagoToken(anyLong(), anyString());
    }
}