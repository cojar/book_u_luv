package com.project.bookuluv.member.oAuth;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberRole;
import com.project.bookuluv.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final SocialAccountRepository socialAccountRepository;
    private final MemberRepository memberRepository; // MemberRepository로 수정

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes: {}", oAuth2User.getAttributes());
        System.out.println(oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo((Map) oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String userName = provider + "_" + providerId; // userName으로 수정
        String nickName = oAuth2UserInfo.getName(); // nickName으로 수정

        // 추가
        Optional<Member> optionalMember = memberRepository.findByUserName(userName); // findByUserName으로 수정
        Member member;

        // 가입된 사용자 정보가 없으면
        if (optionalMember.isEmpty()) {
            member = new Member();
            member.setNickName(nickName); // nickName으로 수정
            member.setUserName(userName); // userName으로 수정
            member = memberRepository.save(member); // 저장
        } else {
            // 가입된 사용자 정보가 있으면
            member = optionalMember.get();
        }
        // 추가 끝

        Optional<SocialAccount> optionalSocialAccount = socialAccountRepository.findByUserName(userName); // findByUserName으로 수정
        SocialAccount socialAccount;

        if (optionalSocialAccount.isEmpty()) {
            socialAccount = SocialAccount.builder()
                    .userName(userName) // userName으로 수정
                    .nickName(nickName) // nickName으로 수정
                    .provider(provider)
                    .providerId(providerId)
                    .member(member) // 사용자 정보 담기 => member_id로 수정
                    .role(MemberRole.USER) // 필요에 따라 수정
                    .build();

            socialAccountRepository.save(socialAccount);
        } else {
            socialAccount = optionalSocialAccount.get();
        }

        UserDetails userDetails = User.builder()
                .username(userName) // userName으로 수정
                .authorities(new ArrayList<>())
                .build();

        return PrincipalDetails.create(userDetails, oAuth2User.getAttributes(), socialAccountRepository);
    }
}
