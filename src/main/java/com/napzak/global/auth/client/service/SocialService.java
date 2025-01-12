package com.napzak.global.auth.client.service;

import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import com.napzak.global.auth.client.dto.StoreLoginRequest;

public interface SocialService {
    StoreSocialInfoResponse login(
            final String authorizationToken,
            final StoreLoginRequest storeLoginRequest);
}
