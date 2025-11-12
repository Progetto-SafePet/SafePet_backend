package it.safepet.backend.autenticazione.service;

import it.safepet.backend.autenticazione.dto.AuthResponseDTO;
import it.safepet.backend.autenticazione.dto.LoginRequestDTO;
import it.safepet.backend.autenticazione.jwt.JwtUtil;
import it.safepet.backend.autenticazione.jwt.Role;
import it.safepet.backend.exception.UnauthorizedException;
import it.safepet.backend.persistence.entity.User;
import it.safepet.backend.persistence.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.EntityNotFoundException;

@Service
@Validated
public class AutenticazioneServiceImpl implements AutenticazioneService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO login(@Valid LoginRequestDTO loginRequestDTO) {
        // Trova utente per email
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con email: " + loginRequestDTO.getEmail()));

        // Verifica password
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenziali non valide: password errata.");
        }

        // Genera token JWT
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), Role.PROPRIETARIO);

        // Restituisce risposta
        return new AuthResponseDTO(token, user.getId(), user.getEmail());
    }
}
