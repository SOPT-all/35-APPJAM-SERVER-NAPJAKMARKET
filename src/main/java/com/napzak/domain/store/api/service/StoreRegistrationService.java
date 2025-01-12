package com.napzak.domain.store.api.service;

import com.napzak.domain.store.core.StoreEntity;
import com.napzak.domain.store.core.Role;
import com.napzak.domain.store.core.StoreRepository;
import com.napzak.global.auth.client.dto.StoreSocialInfoResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRegistrationService {

    private final StoreRepository storeRepository;

    @Transactional
    public Long registerStoreWithStoreInfo(final StoreSocialInfoResponse storeSocialInfoResponse) {

        StoreEntity store = StoreEntity.create(
                null,
                null,
                Role.STORE,
                storeSocialInfoResponse.socialId(),
                storeSocialInfoResponse.socialType(),
                null
        );

        storeRepository.save(store);

        log.info("Store registered with storeId: {}, role: {}", store.getId(), store.getRole());

        return store.getId();
    }
}