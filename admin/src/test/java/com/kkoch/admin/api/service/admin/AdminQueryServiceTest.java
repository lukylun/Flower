package com.kkoch.admin.api.service.admin;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.admin.LoginAdmin;
import com.kkoch.admin.api.controller.admin.response.AdminResponse;
import com.kkoch.admin.api.service.admin.dto.LoginDto;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
class AdminQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    private AdminQueryService adminQueryService;
    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("[관리자 목록 조회]")
    @Test
    void getAdmins() {
        //given
        insertAdmin("admin1", "1234");
        insertAdmin("admin2", "1111");
        insertAdmin("admin3", "2222");
        insertAdmin("admin4", "3333");

        //when
        List<AdminResponse> admins = adminQueryService.getAdmins();

        //then
        assertThat(admins).hasSize(4);
    }

    @DisplayName("[관리자 로그인] 없는 계정 정보")
    @Test
    void loginAdminError() {
        //given
        LoginDto dto = LoginDto.builder()
                .loginId("123admin")
                .loginPw("adminNo")
                .build();

        //when //then

        assertThatThrownBy(() -> adminQueryService.loginAdmin(dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("아이디와 비밀번호를 확인하세요");
    }

    @DisplayName("[관리자 로그인]")
    @Test
    void loginAdmin() {
        //given
        Admin admin = insertAdmin("admin1", "1234");
        LoginDto dto = LoginDto.builder()
                .loginId(admin.getLoginId())
                .loginPw(admin.getLoginPw())
                .build();

        //when
        LoginAdmin loginAdmin = adminQueryService.loginAdmin(dto);

        //then
        assertThat(loginAdmin).isNotNull();
    }

    private Admin insertAdmin(String id, String phone) {
        Admin admin = Admin.builder()
                .name("관리자")
                .loginId(id)
                .loginPw("adminpw")
                .tel("010-1234-" + phone)
                .active(true)
                .position("00")
                .build();
        return adminRepository.save(admin);
    }
}