package com.project.bookuluv.member.oAuth;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Entity
@Table(name = "social_account")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Column
    private String nickName;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Member member;

    public void setUser(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.member = member;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;
}