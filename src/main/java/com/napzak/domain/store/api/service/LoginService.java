package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreEntity;
import com.napzak.domain.store.core.SocialType;
import com.napzak.domain.store.api.dto.LoginSuccessResponse;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreLoginRequest;
import com.napzak.global.auth.client.service.SocialService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final SocialService kakaoSocialService;
    private final StoreRegistrationService storeRegistrationService;
    private final AuthenticationService authenticationService;
    private final StoreService storeService;

    @Transactional
    public LoginSuccessResponse login(
            String authorizationCode,
            StoreLoginRequest request){
        try {
            // 소셜 서비스로부터 사용자 정보 조회
            StoreSocialInfoResponse storeInfo = findStoreInfo(authorizationCode, request);

            // 회원 정보를 DB에서 확인
            Long storeId = findOrRegisterStore(storeInfo);

            // 토큰 발급, 로그인 응답
            return returnLoginSuccessResponse(storeId, storeInfo);
        } catch (Exception e) {
            log.error("Login failed: ", e);
            throw e;
        }
    }
    //소셜 타입에 따라 사용자 정보 조회
    private StoreSocialInfoResponse findStoreInfo(
            String authorizationCode,
            StoreLoginRequest request){

        SocialService socialService = getSocialService(request.socialType());
        return socialService.login(authorizationCode, request);
    }

    //해당하는 소셜 서비스 반환
    private SocialService getSocialService(SocialType socialType){
        return switch (socialType){
            case KAKAO -> kakaoSocialService;
            default -> throw new RuntimeException();
        };
    }

    //기존 회원을 찾거나, 없으면 새로 멤버 등록
    private Long findOrRegisterStore(final StoreSocialInfoResponse storeSocialInfoResponse){
        boolean storeExits = storeService.checkStoreExistsBySocialIdAndSocialType(storeSocialInfoResponse.socialId(),
                storeSocialInfoResponse.socialType());

        if (storeExits){
            StoreEntity store = storeService.findStoreBySocialIdAndSocialType(storeSocialInfoResponse.socialId(),
                    storeSocialInfoResponse.socialType());
            return store.getId();
        }

        return storeRegistrationService.registerStoreWithStoreInfo(storeSocialInfoResponse);
    }

    private LoginSuccessResponse returnLoginSuccessResponse(
            Long storeId,
            StoreSocialInfoResponse storeSocialInfoResponse){

        StoreEntity store = storeService.findStoreByStoreId(storeId);
        return authenticationService.generateLoginSuccessResponse(storeId, storeSocialInfoResponse);

    }

}
