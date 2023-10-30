package com.kkoch.user.api.service.member;

import com.kkoch.user.api.controller.member.response.MemberResponse;
import com.kkoch.user.api.service.member.dto.JoinMemberDto;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isEmpty()) {
            throw new UsernameNotFoundException("등록되지 않는 사용자입니다.");
        }

        Member member = findMember.get();
        return new User(member.getEmail(), member.getEncryptedPwd(),
            true, true, true, true,
            new ArrayList<>()); //권한
    }

    /**
     * 회원 가입
     * @param dto 회원 정보
     * @return 가입된 회원의 정보
     */
    public MemberResponse join(JoinMemberDto dto) {
        dto.setMemberKey(UUID.randomUUID().toString());
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPwd()));

        Member savedMember = memberRepository.save(member);

        return MemberResponse.of(savedMember);
    }

    /**
     * 회원 비밀번호 변경
     *
     * @param memberKey 회원 고유키
     * @param currentPwd 현재 비밀번호
     * @param newPwd 변경할 비밀번호
     * @return 변경된 회원의 정보
     */
    public MemberResponse setPassword(String memberKey, String currentPwd, String newPwd) {
        Member member = getMember(memberKey);

        matchCurrentPwd(currentPwd, member);

        member.changePwd(passwordEncoder.encode(newPwd));

        return MemberResponse.of(member);
    }

    /**
     * 회원 탈퇴
     *
     * @param memberKey 회원 고유키
     * @param pwd 현재 비밀번호
     * @return 탈퇴한 회원의 정보
     */
    public MemberResponse withdrawal(String memberKey, String pwd) {
        Member member = getMember(memberKey);

        matchCurrentPwd(pwd, member);

        member.withdrawal();
        return MemberResponse.of(member);
    }

    public Member getUserDetailsByEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isEmpty()) {
            throw new UsernameNotFoundException("등록되지 않는 사용자입니다.");
        }

        return findMember.get();
    }

    private Member getMember(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
            .orElseThrow(NoSuchElementException::new);
    }

    private void matchCurrentPwd(String currentPwd, Member member) {
        boolean matches = passwordEncoder.matches(currentPwd, member.getEncryptedPwd());
        if (!matches) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
    }
}
