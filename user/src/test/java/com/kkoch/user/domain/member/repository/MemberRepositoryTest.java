package com.kkoch.user.domain.member.repository;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Transactional
class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("이메일로 회원을 조회할 수 있다.")
    @Test
    void findByEmail() {
        //given
        Member member = createMember();

        //when
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        //then
        assertThat(findMember).isPresent();
    }

    @DisplayName("회원 고유키로 회원을 조회할 수 있다.")
    @Test
    void findByMemberKey() {
        //given
        Member member = createMember();

        //when
        Optional<Member> findMember = memberRepository.findByMemberKey(member.getMemberKey());

        //then
        assertThat(findMember).isPresent();
    }

    private Member createMember() {
        Member member = Member.builder()
            .email("ssafy@ssafy.com")
            .encryptedPwd(passwordEncoder.encode("ssafy1234!"))
            .name("김싸피")
            .tel("010-1234-1234")
            .businessNumber("123-12-12345")
            .point(0)
            .active(true)
            .memberKey(UUID.randomUUID().toString())
            .build();
        return memberRepository.save(member);
    }
}