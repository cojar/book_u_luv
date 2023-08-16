package com.project.bookuluv.member.oAuth;

import com.project.bookuluv.member.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class MemberContext extends User implements OAuth2User {
    // 소셜계정으로 가입한 유저의 정보를 표시할 때 연결해주는 곳
    private final Long id;
    private final String userName;
    private final String nickName;
    private final String firstname;
    private final String lastname;
    private final String phone;
    private final String email;
    private final LocalDateTime createDate;
    private final String imgFilePath;
    private final Boolean gender;
    private final LocalDate birthDate;
    private final String postalNum;
    private final String roadAddress;
    private final String jibunAddress;
    private final String detailAddress;
    private final String extraAddress;
    private Map<String, Object> attributes;
    private String userNameAttributeName;


    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUserName(), member.getPassword(), authorities);
        this.id = member.getId();
        this.userName = member.getUserName();
        this.nickName = member.getNickName();
        this.firstname = member.getFirstName();
        this.lastname = member.getLastName();
        this.phone = member.getPhone();
        this.email = member.getSocialEmail();
        this.birthDate = member.getBirthDate();
        this.imgFilePath = member.getImgFilePath();
        this.gender = member.getGender();
        this.roadAddress = member.getRoadAddress();
        this.jibunAddress = member.getJibunAddress();
        this.detailAddress = member.getDetailAddress();
        this.extraAddress = member.getExtraAddress();
        this.postalNum = member.getPostalNum();
        this.createDate = member.getCreateDate();
    }

    public MemberContext(Member member, List<GrantedAuthority> authorities, Map<String, Object> attributes, String userNameAttributeName) {
        this(member, authorities);
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream().collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getAttribute(this.userNameAttributeName).toString();
    }
}