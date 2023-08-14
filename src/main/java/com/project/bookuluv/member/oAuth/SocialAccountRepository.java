package com.project.bookuluv.member.oAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    SocialAccount findByProviderAndProviderId(String provider, String providerId);

//    Optional<SocialAccount> findByLoginId(String loginId);

    Optional<SocialAccount> findByUserName(String userName);
}