package com.kkoch.admin.domain.admin.repository;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.controller.admin.LoginAdmin;
import com.kkoch.admin.api.controller.admin.response.AdminResponse;
import com.kkoch.admin.domain.admin.Admin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class AdminQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AdminQueryRepository adminQueryRepository;
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
        insertAdmin("admin5", "4444");

        //when
        List<AdminResponse> admins = adminQueryRepository.getAdmins();

        //then
        assertThat(admins).hasSize(5);
    }

    @DisplayName("[관리자  로그인]")
    @Test
    void getLoginAdmin() {
        //given
        Admin admin = insertAdmin("admin1", "1234");

        //when
        Optional<LoginAdmin> loginAdmin = adminQueryRepository.getLoginAdmin(admin.getLoginId(), admin.getLoginPw());

        //then
        assertThat(loginAdmin).isPresent();
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