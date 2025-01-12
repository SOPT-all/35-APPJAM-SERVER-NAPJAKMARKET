package com.napzak.global.auth.client.dto;

import com.napzak.domain.store.core.SocialType;

public record StoreSocialInfoResponse(
        Long socialId,
        SocialType socialType
) {
        public static StoreSocialInfoResponse of(
                final Long socialId,
                final SocialType socialType
        ){
            return new StoreSocialInfoResponse(socialId, socialType);
        }
}
