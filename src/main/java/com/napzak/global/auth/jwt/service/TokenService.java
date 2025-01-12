package com.napzak.global.auth.jwt.service;

import com.napzak.global.common.exception.NapzakException;
import org.springframework.stereotype.Service;

import com.napzak.global.auth.jwt.exception.TokenErrorCode;
import com.napzak.global.auth.jwt.repository.TokenRepository;
import com.napzak.global.auth.redis.Token;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

	private final TokenRepository tokenRepository;

	@Transactional
	public void saveRefreshToken(final Long memberId, final String refreshToken) {
		tokenRepository.save(Token.of(memberId, refreshToken));
	}

	public Long findIdByRefreshToken(final String refreshToken) {
		Token token = tokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NapzakException(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND) {
			});

		return token.getId();
	}

	@Transactional
	public void deleteRefreshToken(final Long memberId) {
		Token token = tokenRepository.findById(memberId)
			.orElseThrow(() -> new NapzakException(TokenErrorCode.REFRESH_TOKEN_NOT_FOUND) {
			});

		tokenRepository.delete(token);
		log.info("Deleted refresh token: {}", token);
	}
}
