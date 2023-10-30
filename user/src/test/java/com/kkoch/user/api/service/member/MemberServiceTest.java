package com.kkoch.user.api.service.member;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.api.controller.member.response.MemberResponse;
import com.kkoch.user.api.service.member.dto.JoinMemberDto;
import com.kkoch.user.domain.member.Member;
import com.kkoch.user.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;

@Transactional
class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원 정보를 입력받아 회원가입을 할 수 있다.")
    @Test
    void join() {
        //given
        JoinMemberDto dto = JoinMemberDto.builder()
            .email("ssafy@ssafy.com")
            .pwd("ssafyc204!")
            .name("김싸피")
            .tel("010-1234-1234")
            .businessNumber("123-12-12345")
            .build();

        //when
        MemberResponse response = memberService.join(dto);

        //then
        Optional<Member> findMember = memberRepository.findByMemberKey(response.getMemberKey());
        assertThat(findMember).isPresent();
        assertThat(response)
            .extracting("email", "name")
            .containsExactlyInAnyOrder(
                "ssafy@ssafy.com", "김싸피"
            );
    }

    @DisplayName("회원 비밀번호 변경시 현재 비밀번호가 일치하지 않는다면 예외가 발생한다.")
    @Test
    void setPasswordWithNotEqualCurrentPwd() {
        //given
        Member member = createMember();

        //when //then
        assertThatThrownBy(() -> memberService.setPassword(member.getMemberKey(), "password1!", "newPwd1!"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("현재 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("회원은 비밀번호를 변경할 수 있다.")
    @Test
    void setPassword() {
        //given
        Member member = createMember();
        String currentPwd = "ssafy1234!";
        String newPwd = "newPwd1234!";

        //when
        MemberResponse response = memberService.setPassword(member.getMemberKey(), currentPwd, newPwd);

        //then
        Optional<Member> findMember = memberRepository.findByMemberKey(response.getMemberKey());
        assertThat(findMember).isPresent();
        boolean result = passwordEncoder.matches(newPwd, findMember.get().getEncryptedPwd());
        assertThat(result).isTrue();
    }

    @DisplayName("회원 탈퇴시 현재 비밀번호가 일치하지 않는다면 예외가 발생한다.")
    @Test
    void withdrawalWithNotEqualCurrentPwd() {
        //given
        Member member = createMember();

        //when //then
        assertThatThrownBy(() -> memberService.withdrawal(member.getMemberKey(), "password1!"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("현재 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("회원은 회원 탈퇴를 할 수 있다.")
    @Test
    void withdrawal() {
        //given
        Member member = createMember();
        String currentPwd = "ssafy1234!";

        //when
        MemberResponse response = memberService.withdrawal(member.getMemberKey(), currentPwd);

        //then
        Optional<Member> findMember = memberRepository.findByMemberKey(response.getMemberKey());
        assertThat(findMember).isPresent();
        assertThat(findMember.get().isActive()).isFalse();
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