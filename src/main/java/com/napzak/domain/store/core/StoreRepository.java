package com.napzak.domain.store.core;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    @Query("SELECT u FROM StoreEntity u WHERE u.socialId = :socialId AND u.socialType = :socialType")
    Optional<StoreEntity> findBySocialTypeAndSocialId(@Param("socialId") Long socialId,
                                                      @Param("socialType") SocialType socialType);
}
