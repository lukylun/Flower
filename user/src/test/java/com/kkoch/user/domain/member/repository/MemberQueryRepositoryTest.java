package com.kkoch.user.domain.member.repository;

import com.kkoch.user.IntegrationTestSupport;
import com.kkoch.user.api.controller.member.response.MemberInfoResponse;
import com.kkoch.user.api.controller.member.response.MemberResponseForAdmin;
import com.kkoch.user.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Transactional
class MemberQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @DisplayName("회원은 본인의 정보를 조회할 수 있다.")
    @Test
    void findMyInfoByMemberKey() {
        //given
        Member member = createMember();

        //when
        MemberInfoResponse memberInfo = memberQueryRepository.findMyInfoByMemberKey(member.getMemberKey());

        //then
        assertThat(memberInfo).isNotNull();
    }

    @DisplayName("이메일이 존재하면 true를 반환한다.")
    @Test
    void existEmail() {
        //given
        Member member = createMember();

        //when
        Boolean result = memberQueryRepository.existEmail(member.getEmail());

        //then
        assertThat(result).isTrue();
    }
    @DisplayName("전체 회원을 조회 할 수 있다.")
    @Test
    void getUsers() throws Exception {
        //given
        Member member = createMember();
        //when

        List<MemberResponseForAdmin> responses = memberQueryRepository.findAllUser();
        //then
        assertThat(responses).hasSize(1);
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