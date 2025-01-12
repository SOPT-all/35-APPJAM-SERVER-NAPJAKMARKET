package com.napzak.global.auth.client.service;


import com.napzak.domain.store.core.SocialType;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreLoginRequest;
import com.napzak.global.auth.client.kakao.KakaoApiClient;
import com.napzak.global.auth.client.kakao.KakaoAuthApiClient;
import com.napzak.global.auth.client.kakao.dto.KakaoAccessTokenResponse;
import com.napzak.global.auth.client.kakao.dto.KakaoUserResponse;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoSocialService implements SocialService {

    private static final String AUTH_CODE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;

    @Transactional
    @Override
    public StoreSocialInfoResponse login(
            final String authorizationCode,
            final StoreLoginRequest loginRequest
    ){
            String accessToken;
            try{
                accessToken = getOAuth2Authentication(authorizationCode);
            }catch(FeignException e){
                throw new RuntimeException();
            }
            return getLoginDto(loginRequest.socialType(), getUserInfo(accessToken));
    }

    private String getOAuth2Authentication(
            final String authorizationCode
    ){
        KakaoAccessTokenResponse response = kakaoAuthApiClient.getOAuth2AccessToken(
                AUTH_CODE,
                clientId,
                redirectUri,
                authorizationCode
        );
        return response.accessToken();
    }

    private KakaoUserResponse getUserInfo(
            final String accessToken
    ){
        return kakaoApiClient.getUserInformation("Bearer "+accessToken);
    }

    private StoreSocialInfoResponse getLoginDto(
            final SocialType socialType,
            final KakaoUserResponse kakaoUserResponse
    ){
        return StoreSocialInfoResponse.of(
                kakaoUserResponse.id(),
                socialType
        );
    }



}
