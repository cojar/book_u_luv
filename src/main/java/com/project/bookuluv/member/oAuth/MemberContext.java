package com.project.bookuluv.member.oAuth;

import com.project.bookuluv.member.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
    private final String email;
    private final String nickName;
    private final String imgFilePath;
    private final LocalDateTime createDate;
    private Map<String, Object> attributes;
    private String userNameAttributeName;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUserName(), member.getPassword(), authorities);
        this.id = member.getId();
        this.email = member.getSocialEmail();
        this.nickName = member.getNickName();
        this.createDate = member.getCreateDate();
        this.imgFilePath = member.getImgFilePath();
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