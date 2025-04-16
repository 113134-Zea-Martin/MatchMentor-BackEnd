package com.scaffold.template.services;

import com.scaffold.template.entities.PasswordResetToken;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.PasswordResetTokenRepository;
import com.scaffold.template.repositories.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * Servicio para gestionar los tokens de restablecimiento de contraseña.
 * Este servicio permite crear, validar y usar tokens para restablecer contraseñas.
 */
@Service
public class PasswordResetTokenService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Constructor del servicio.
     *
     * @param userRepository repositorio para gestionar usuarios.
     * @param passwordResetTokenRepository repositorio para gestionar tokens de restablecimiento de contraseña.
     * @param passwordEncoder codificador de contraseñas.
     */
    @Autowired
    public PasswordResetTokenService(UserRepository userRepository,
                                     PasswordResetTokenRepository passwordResetTokenRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un token de restablecimiento de contraseña para un usuario.
     *
     * @param user el usuario para el que se generará el token.
     * @return el token generado.
     */
    public String createTokenForUser(UserEntity user) {

        // Eliminar tokens existentes para el usuario
        passwordResetTokenRepository.deleteByUser(user);

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // Guarda el token en la base de datos
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetToken.setUsed(false);

        passwordResetTokenRepository.save(passwordResetToken);

        return token;
    }

    /**
     * Valida un token de restablecimiento de contraseña.
     *
     * @param token el token a validar.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return false;
        }

        PasswordResetToken passwordResetToken = optionalToken.get();
        if (passwordResetToken.isUsed()) {
            return false;
        }
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }

    }

    /**
     * Restablece la contraseña de un usuario utilizando un token válido.
     *
     * @param token el token de restablecimiento de contraseña.
     * @param newPassword la nueva contraseña a establecer.
     * @return true si la contraseña fue restablecida correctamente, false en caso contrario.
     */
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return false;
        }

        PasswordResetToken passwordResetToken = optionalToken.get();
        if (!validateToken(token)) {
            return false;
        }

        UserEntity user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);

        return true;
    }

}