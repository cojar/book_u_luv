package com.project.bookuluv.domain.member.oAuth;

import com.project.bookuluv.domain.member.domain.Member;
import com.project.bookuluv.domain.member.exception.MemberNotFoundException;
import com.project.bookuluv.domain.member.exception.OAuthTypeMatchNotFoundException;
import com.project.bookuluv.domain.member.repository.MemberRepository;
import com.project.bookuluv.domain.member.dto.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        String oauthId = null;

        Member member = null;
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        if (!"KAKAO".equals(oauthType)
                && !"NAVER".equals(oauthType)
                && !"GOOGLE".equals(oauthType)) {
            throw new OAuthTypeMatchNotFoundException();

        } else if ("KAKAO".equals(oauthType)) {
            oauthId = oAuth2User.getName();

        } else if ("NAVER".equals(oauthType)) {
            Map attributesResponse = (Map) attributes.get("response");
            oauthId = (String) attributesResponse.get("id");

        } else if ("GOOGLE".equals(oauthType)) {
            oauthId = oAuth2User.getName();
        }
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = "";


        if (isNew(oauthType, oauthId)) {
            switch (oauthType) {
                case "KAKAO" -> {
                    Map attributesProperties = (Map) attributes.get("properties");
                    Map attributesKakaoAccount = (Map) attributes.get("kakao_account");
                    String nickName = (String) attributesProperties.get("nickname");
                    String profileImgUrl = (String) attributesProperties.get("profile_image");
                    String thumbnail_image = (String) attributesProperties.get("thumbnail_image");
                    providerId = oauthId;
                    String email = "%s@kakao.com".formatted(oauthId);
                    String userName = "KAKAO_%s".formatted(oauthId);
                    Boolean gender = null;
                    String age_range = null;
                    LocalDate birthDate = null;
                    Boolean mailAuth = null;
                    boolean isActive = true;


                    if ((boolean) attributesKakaoAccount.get("has_email")) { // 만약 이메일 공개 동의 시 수집 가능
                        email = (String) attributesKakaoAccount.get("email");
                        mailAuth = true;
                    }
                    if ((boolean) attributesKakaoAccount.get("has_age_range") // 만약 연령대 공개 동의 시 수집 가능
                            && !(boolean) attributesKakaoAccount.get("age_range_needs_agreement")) {
                        age_range = (String) attributesKakaoAccount.get("age_range");
                    }
                    if ((boolean) attributesKakaoAccount.get("has_birthday") // 만약 생년월일 공개 동의 시 수집 가능
                            && !(boolean) attributesKakaoAccount.get("birthday_needs_agreement")) {
                        birthDate = (LocalDate) attributesKakaoAccount.get("birthday");
                    }
                    if ((boolean) attributesKakaoAccount.get("has_gender") // 만약 성별 공개 동의 시 수집 가능
                            && !(boolean) attributesKakaoAccount.get("gender_needs_agreement")) {
                        gender = (boolean) attributesKakaoAccount.get("gender");
                    }

                    String fullName = (String) attributesProperties.get("nickname");
                    String lastname;
                    String firstname;
                    if (fullName.length() == 2 && isSpecialLastname(fullName)) {
                        lastname = fullName.substring(0, 2);
                        firstname = "";
                    } else if (fullName.length() == 3 && isSpecialLastname(fullName.substring(0, 2))) {
                        lastname = fullName.substring(0, 2);
                        firstname = fullName.substring(2);
                    } else if (fullName.length() == 4 && isSpecialLastname(fullName.substring(0, 2))) {
                        lastname = fullName.substring(0, 2);
                        firstname = fullName.substring(2);
                    } else {
                        lastname = fullName.substring(0, 1);
                        firstname = fullName.substring(1);
                    }
                    // 저장
                    member = Member.builder()
                            .userName(userName)                     // 사용자ID 추가(email형식)
                            .password("")
                            .nickName(nickName)                     // 사용자 닉네임 추가
                            .socialEmail(email)                     // 사용자 이메일 추가
                            .imgFilePath(profileImgUrl)             // 사용자 프로필 사진 추가
                            .createDate(LocalDateTime.now())        // 계정 생성일 추가
                            .birthDate(birthDate)                   // 사용자 생년월일 추가
                            .role(MemberRole.valueOf("MEMBER")) // 사용자 권한 추가
                            .mailAuth(mailAuth)                     // 사용자 메일 인증여부 추가(일반 가입시 true)
                            .gender(gender)                         // 사용자 성별 추가
                            .firstName(firstname)               // 사용자 이름 추가
                            .lastName(lastname)                 // 사용자 성 추가
                            .provider(provider)
                            .providerId(providerId)
                            .isActive(isActive)                     // 계정 활성 여부 추가
                            .build(); // 빌드완료
                    memberRepository.save(member);
                }
                case "NAVER" -> {
                    Map attributesResponse = (Map) attributes.get("response");
                    String phone_e164 = (String) attributesResponse.get("mobile_e164");
                    String age = (String) attributesResponse.get("age");
                    String id = (String) attributesResponse.get("id");
                    String userName = "NAVER_%s".formatted(id);
                    providerId = oauthId;
                    String profileImgUrl;
                    String nickName;
                    LocalDate birthDate;
                    Boolean gender = null;
                    boolean mailAuth = false;
                    boolean isActive = true;
                    String lastname;
                    String firstname;

                    String email;
                    if (attributesResponse.containsKey("email")) {
                        email = (String) attributesResponse.get("email");
                        mailAuth = true;
                    } else {
                        email = "%s@naver.com".formatted(oauthId);
                    }


                    if (attributesResponse.containsKey("nickname")) {
                        nickName = (String) attributesResponse.get("nickname");
                    } else {
                        nickName = null;
                    }


                    if (attributesResponse.containsKey("profile_image")) {
                        profileImgUrl = (String) attributesResponse.get("profile_image");
                    } else {
                        profileImgUrl = null;
                    }


                    if (attributesResponse.containsKey("birthday") && attributesResponse.containsKey("birthyear")) {
                        String birthday = (String) attributesResponse.get("birthday");
                        String birthyear = (String) attributesResponse.get("birthyear");
                        String combinedDate = String.format("%s-%s", birthyear, birthday);
                        birthDate = LocalDate.parse(combinedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } else {
                        birthDate = null;
                    }


                    String naverGender = (String) attributesResponse.get("gender");
                    if (naverGender.equals("M")) {
                        gender = true;
                    } else if (naverGender.equals("F")) {
                        gender = false;
                    }
                    String phone = (String) attributesResponse.get("mobile");
                    String sanitizedPhone = phone.replaceAll("-", "");


                    String fullName = (String) attributesResponse.get("name");
                    if (fullName.length() == 2 && isSpecialLastname(fullName)) {
                        lastname = fullName.substring(0, 2);
                        firstname = "";
                    } else if (fullName.length() == 3 && isSpecialLastname(fullName.substring(0, 2))) {
                        lastname = fullName.substring(0, 2);
                        firstname = fullName.substring(2);
                    } else if (fullName.length() == 4 && isSpecialLastname(fullName.substring(0, 2))) {
                        lastname = fullName.substring(0, 2);
                        firstname = fullName.substring(2);
                    } else {
                        lastname = fullName.substring(0, 1);
                        firstname = fullName.substring(1);
                    }

                    // 저장
                    member = Member.builder()
                            .userName(userName)                 // 사용자ID 추가(email형식)
                            .password("")
                            .nickName(nickName)                 // 사용자 닉네임 추가
                            .socialEmail(email)
                            .imgFilePath(profileImgUrl)
                            .createDate(LocalDateTime.now())    // 계정 생성일 추가
                            .birthDate(birthDate)               // 사용자 생년월일 추가
                            .phone(sanitizedPhone)              // 사용자 연락처 추가
                            .mailAuth(mailAuth)                 // 사용자 메일 인증여부 추가(일반 가입시 true)
                            .firstName(firstname)               // 사용자 이름 추가
                            .lastName(lastname)                 // 사용자 성 추가
                            .gender(gender)                     // 사용자 성별 추가
                            .provider(provider)
                            .providerId(providerId)
                            .role(MemberRole.valueOf("MEMBER")) // 사용자 권한 추가
                            .isActive(isActive)                     // 계정 활성 여부 추가
                            .build(); // 빌드완료
                    memberRepository.save(member);
                }
                case "GOOGLE" -> {
                    Map gattributes = (Map) attributes;
                    String id = (String) gattributes.get("sub");
                    String userName = "GOOGLE_%s".formatted(id);
                    providerId = oauthId;
                    String profileImgUrl = (String) gattributes.get("picture");
                    String nickName = (String) gattributes.get("name");
                    String email = (String) gattributes.get("email");
                    String firstname = (String) gattributes.get("given_name");
                    String lastname = (String) gattributes.get("family_name");
                    Boolean gender = null;
                    boolean mailAuth = true;
                    boolean isActive = true;

                    // 저장
                    member = Member.builder()
                            .userName(userName)                 // 사용자ID 추가(email형식) sub+google
                            .password("")
                            .nickName(nickName)                 // 사용자 닉네임 추가 name
                            .socialEmail(email)                 // 사용자 이메일 추가 email
                            .imgFilePath(profileImgUrl)         // 사용자 프로필 추가 picture
                            .createDate(LocalDateTime.now())    // 계정 생성일 추가
                            .mailAuth(mailAuth)                 // 사용자 메일 인증여부 추가(일반 가입시 true)
                            .firstName(firstname)               // 사용자 이름 추가 given_name
                            .lastName(lastname)                 // 사용자 성 추가   family_name
                            .gender(gender)                     // 사용자 성별 추가
                            .provider(provider)
                            .providerId(providerId)
                            .role(MemberRole.valueOf("MEMBER")) // 사용자 권한 추가
                            .isActive(isActive)                     // 계정 활성 여부 추가
                            .build(); // 빌드완료
                    memberRepository.save(member);
                }
            }
        } else {
            member = memberRepository.findByUserName("%s_%s".formatted(oauthType, oauthId))
                    .orElseThrow(MemberNotFoundException::new);
            if (!member.isActive()) {
                throw new DisabledException("해당 회원은 비활성화 되었습니다.");
            }
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));
        return new MemberContext(member, authorities, attributes, userNameAttributeName);
    }

    private boolean isNew(String oAuthType, String oauthId) {
        return memberRepository.findByUserName("%s_%s".formatted(oAuthType, oauthId)).isEmpty();
    }
    private boolean isSpecialLastname (String lastname){
        String[] specialLastnames = {"남궁", "황보", "제갈", "사공", "선우", "서문", "독고", "동방", "어금", "망절", "무본", "황목", "등정", "장곡", "강전"};
        for (String special : specialLastnames) {
            if (special.equals(lastname)) {
                return true;
            }
        }
        return false;
    }
}