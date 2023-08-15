package com.project.bookuluv.member.oAuth;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.exception.MemberNotFoundException;
import com.project.bookuluv.member.exception.OAuthTypeMatchNotFoundException;
import com.project.bookuluv.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String oauthId = oAuth2User.getName();

        Member member = null;
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        if (!"KAKAO".equals(oauthType)) {
            throw new OAuthTypeMatchNotFoundException();
        }

        if (isNew(oauthType, oauthId)) {
            switch (oauthType) {
                case "KAKAO" -> {
                    Map attributesProperties = (Map) attributes.get("properties");
                    Map attributesKakaoAcount = (Map) attributes.get("kakao_account");
                    String nickName = (String) attributesProperties.get("nickname");
                    String profileImgUrl = (String) attributesProperties.get("profile_image");
                    String thumbnail_image = (String) attributesProperties.get("thumbnail_image");
                    String email = "%s@kakao.com".formatted(oauthId);
                    String userName = "KAKAO_%s".formatted(oauthId);

                    if ((boolean) attributesKakaoAcount.get("has_email")) {
                        email = (String) attributesKakaoAcount.get("email");
                    }
                    // 저장
                    member = Member.builder()
                            .userName(userName)                 // 사용자ID 추가(email형식)
                            .password("")
                            .nickName(nickName)                 // 사용자 닉네임 추가
                            .socialEmail(email)
                            .imgFilePath(profileImgUrl)
                            .createDate(LocalDateTime.now())    // 계정 생성일 추가
//                            .roadAddress(roadAddress)           // 사용자 도로명주소 추가(카카오API)
//                            .jibunAddress(jibunAddress)         // 사용자 지번주소 추가(카카오API)
//                            .detailAddress(detailAddress)       // 사용자 상세주소 추가(동네이름 / 아파트이름)(카카오API)
//                            .extraAddress(extraAddress)         // 사용자 기타주소 추가(ex. 동, 호수) (카카오API)
//                            .postalNum(postalNum)               // 사용자 우편번호 추가
//                            .phone(phone)                       // 사용자 연락처 추가
//                            .firstName(firstName)               // 사용자 이름 추가
//                            .lastName(lastName)                 // 사용자 성 추가
//                            .gender(gender)                     // 사용자 성별 추가
//                            .birthDate(birthDate)               // 사용자 생년월일 추가
//                            .mailKey(mailKey)                   // 사용자 이메일 인증당시 인증 키 추가
//                            .role(role)                         // 사용자 권한 추가
//                            .mailAuth(mailAuth)                 // 사용자 메일 인증여부 추가(일반 가입시 true)
                            .build(); // 빌드완료
                    memberRepository.save(member);
                }
            }
        } else {
            member = memberRepository.findByUserName("%s_%s".formatted(oauthType, oauthId))
                    .orElseThrow(MemberNotFoundException::new);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));
        return new MemberContext(member, authorities, attributes, userNameAttributeName);
    }

    private boolean isNew(String oAuthType, String oAuthId) {
        return memberRepository.findByUserName("%s_%s".formatted(oAuthType, oAuthId)).isEmpty();
    }
}