package com.napzak.domain.store.api.controller;

import com.napzak.domain.store.api.dto.StoreLoginResponse;
import com.napzak.global.auth.annotation.CurrentMember;
import com.napzak.global.auth.client.dto.StoreLoginRequest;
import com.napzak.global.common.exception.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface StoreApi {

    ResponseEntity<SuccessResponse<StoreLoginResponse>> login(
            @RequestParam String authorizationCode,
            @RequestBody StoreLoginRequest body,
            HttpServletResponse httpServletResponse
            );

    ResponseEntity<SuccessResponse<Void>> logOut(
            @CurrentMember final Long storeId
    );
}
