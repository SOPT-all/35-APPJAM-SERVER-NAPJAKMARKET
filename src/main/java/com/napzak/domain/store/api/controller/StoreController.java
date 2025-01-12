package com.napzak.domain.store.api.controller;

import com.napzak.domain.store.api.service.LoginService;
import com.napzak.domain.store.api.dto.LoginSuccessResponse;
import com.napzak.domain.store.api.dto.StoreLoginResponse;
import com.napzak.domain.store.core.exception.StoreSuccessCode;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreLoginRequest;
import com.napzak.global.auth.jwt.service.TokenService;
import com.napzak.global.common.exception.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/stores")
@RequiredArgsConstructor
public class StoreController implements StoreApi {

    private final LoginService loginService;
    private final TokenService tokenService;

    private static final String REFRESH_TOKEN = "refreshToken";
    private static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    @PostMapping("/login")
    @Override
    public ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
            String authorizationCode,
            StoreLoginRequest storeLoginRequest,
            HttpServletResponse httpServletResponse
    ){
            LoginSuccessResponse successResponse = loginService.login(authorizationCode, storeLoginRequest);
            ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, successResponse.refreshToken())
                    .maxAge(COOKIE_MAX_AGE)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            httpServletResponse.setHeader("Set-Cookie", cookie.toString());

            StoreLoginResponse response = StoreLoginResponse.of(
                    successResponse.accessToken(),
                    successResponse.nickname(),
                    successResponse.role());

            return ResponseEntity.ok()
                    .body(SuccessResponse.of(StoreSuccessCode.LOGIN_SUCCESS, response));
        }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<SuccessResponse<Void>> logOut(
            @CurrentMember final Long storeId
    ){
        tokenService.deleteRefreshToken(storeId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(StoreSuccessCode.LOGOUT_SUCCESS, null));
    }
}
