package com.project.bookuluv.member.service;

import com.project.bookuluv.member.domain.Member;
import com.project.bookuluv.member.dto.MemberRole;
import com.project.bookuluv.member.exception.AppException;
import com.project.bookuluv.member.exception.DataNotFoundException;
import com.project.bookuluv.member.exception.ErrorCode;
import com.project.bookuluv.member.repository.MemberRepository;
import com.project.bookuluv.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l;

    public Member getUser(String userName) {
        Optional<Member> member = this.memberRepository.findByUserName(userName);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }

    public String join(String userName,
                       String password,
                       String nickName,
                       String roadAddress,
                       String jibunAddress,
                       String detailAddress,
                       String extraAddress,
                       String postalNum,
                       String phone,
                       String firstName,
                       String lastName,
                       Boolean gender,
                       LocalDate birthDate,
                       Integer mailKey,
                       MemberRole role,
                       boolean mailAuth) { // 프로필 사진 파일 이름

        // userName 중복체크
        memberRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + " 는 존재합니다.");
                });

        // 저장
        Member member = Member.builder()
                .userName(userName)                 // 사용자ID 추가(email형식)
                .password(encoder.encode(password)) // 사용자 비밀번호 추가(passwordEncoder로 인코딩)
                .nickName(nickName)                 // 사용자 닉네임 추가
                .roadAddress(roadAddress)           // 사용자 도로명주소 추가(카카오API)
                .jibunAddress(jibunAddress)         // 사용자 지번주소 추가(카카오API)
                .detailAddress(detailAddress)       // 사용자 상세주소 추가(동네이름 / 아파트이름)(카카오API)
                .extraAddress(extraAddress)         // 사용자 기타주소 추가(ex. 동, 호수) (카카오API)
                .postalNum(postalNum)               // 사용자 우편번호 추가
                .phone(phone)                       // 사용자 연락처 추가
                .firstName(firstName)               // 사용자 이름 추가
                .lastName(lastName)                 // 사용자 성 추가
                .gender(gender)                     // 사용자 성별 추가
                .birthDate(birthDate)               // 사용자 생년월일 추가
                .mailKey(mailKey)                   // 사용자 이메일 인증당시 인증 키 추가
                .role(role)                         // 사용자 권한 추가
                .mailAuth(mailAuth)                 // 사용자 메일 인증여부 추가(일반 가입시 true)
                .createDate(LocalDateTime.now()) // 계정 생성일 추가
                .isActive(true)
                .build(); // 빌드완료

        this.memberRepository.save(member);
        return "SUCCESS";
    }

    public Member verifyEmailConfirmation(String userName, int mailKey) throws Exception {
        Member member = this.getUserByUserName(userName);
        if (member == null) {
            throw new Exception("유효하지 않은 이메일입니다.");
        }
        if (member.isMailAuth()) {
            throw new Exception("이미 인증된 이메일입니다.");
        }
        if (member.getMailKey() != mailKey) {
            throw new Exception("인증코드가 일치하지 않습니다.");
        }
        member.setMailAuth(true);
        memberRepository.save(member);
        return member;
    }

    public Member getUserByUserName(String userName) {
        Optional<Member> memberOp = this.memberRepository.findByUserName(userName);
        return memberOp.orElse(null);
    }

//    public void updateMailAuth(String userName, int mailKey) {
//        int updatedRows = memberRepository.updateMailAuth(userName, mailKey);
//        if (updatedRows > 0) {
//            System.out.println("Mail auth updated successfully.");
//        } else {
//            System.out.println("Failed to update mail auth.");
//        }
//    }

    public Member updateProfile(Member member, MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") +
                File.separator + "src" +
                File.separator + "main" +
                File.separator + "resources" +
                File.separator + "static" +
                File.separator + "files";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        String filePath = "/files/" + fileName;

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile); // 업로드된 파일 저장

        member.setImgFileName(fileName);
        member.setImgFilePath(filePath);
        memberRepository.save(member);

        return member;
    }

    public String generateTempPassword() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }


    // 이하 JWT 토큰 관련
    public String login(String userName, String password) {

        // userName 없음
        Member user = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        // password 틀림
        // if (!encoder.matches(user.getPassword(), password)) {
        //    throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        //  }

        String token = JwtUtil.createJwt(user.getUserName(), secretKey, expiredMs);
        return token;
    }

    public Member getMember(String userName) {
        Optional<Member> siteUser = this.memberRepository.findByUserName(userName);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }

    public Member getMember(String firstName, String lastName, String phone) {
        Optional<Member> member = this.memberRepository.findByFirstNameAndLastNameAndPhone(firstName, lastName, phone);
        if (member.isPresent()) {
            return member.get();
        }
        throw new DataNotFoundException("Member not found");
    }

    public Member getMember(String firstName, String lastName, String phone, String userName) {
        Optional<Member> member = this.memberRepository.findByFirstNameAndLastNameAndPhoneAndUserName(firstName, lastName, phone, userName);
        if (member.isPresent()) {
            return member.get();
        }
        throw new DataNotFoundException("Member not found");
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public void deactivateMember(Member member) {
        member.deactivate();
        this.memberRepository.save(member);
    }


    // public String me(String userName) {
    //  User user = memberRepository.findByUserName(userName)
    //           .orElseThrow();
    //   return ;
    // }
}
