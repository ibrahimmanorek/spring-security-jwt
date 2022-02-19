package com.bezkoder.spring.security.jwt.services;

import java.time.Instant;
import java.util.Optional;

import com.bezkoder.spring.security.jwt.models.User;
import com.bezkoder.spring.security.jwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.security.jwt.exception.TokenRefreshException;
import com.bezkoder.spring.security.jwt.models.RefreshToken;
import com.bezkoder.spring.security.jwt.repository.RefreshTokenRepository;
import com.bezkoder.spring.security.jwt.repository.UserRepository;

@Service
public class RefreshTokenService {
  @Value("${config.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    Optional<User> optionalUser = userRepository.findById(userId);
    refreshToken.setUser(optionalUser.get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
//    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setToken(jwtUtils.generateRefreshTokenFromUsername(optionalUser.get().getUsername()));
    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}
