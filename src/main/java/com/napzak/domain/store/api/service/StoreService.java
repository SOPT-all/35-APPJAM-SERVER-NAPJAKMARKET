package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreEntity;
import com.napzak.domain.store.core.SocialType;
import com.napzak.domain.store.core.exception.StoreErrorCode;
import com.napzak.domain.store.core.StoreRepository;
import com.napzak.global.common.exception.NapzakException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public StoreEntity findStoreByStoreId(Long StoreId) {
        return storeRepository.findById(StoreId)
                .orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean checkStoreExistsBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return storeRepository.findBySocialTypeAndSocialId(socialId, socialType).isPresent();
    }

    @Transactional(readOnly = true)
    public StoreEntity findStoreBySocialIdAndSocialType(final Long socialId, final SocialType socialType) {
        return storeRepository.findBySocialTypeAndSocialId(socialId, socialType)
                .orElseThrow(() -> new NapzakException(StoreErrorCode.STORE_NOT_FOUND));
    }

}