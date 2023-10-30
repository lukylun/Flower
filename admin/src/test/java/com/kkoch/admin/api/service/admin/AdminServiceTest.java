package com.kkoch.admin.api.service.admin;

import com.kkoch.admin.IntegrationTestSupport;
import com.kkoch.admin.api.service.admin.dto.AddAdminDto;
import com.kkoch.admin.api.service.admin.dto.EditAdminDto;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
class AdminServiceTest extends IntegrationTestSupport {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @DisplayName("[관계자 등록]")
    @Test
    public void addAdminTest() {
        // given
        AddAdminDto addAdminDto = AddAdminDto.builder()
                .loginId("soo0300")
                .loginPw("0300soo0989")
                .tel("010-1234-5678")
                .name("kim")
                .position("경매")
                .build();

        // when
        Long adminId = adminService.addAdmin(addAdminDto);

        // then
        Optional<Admin> admin = adminRepository.findById(adminId);
        Assertions.assertThat(admin).isPresent();
    }

    @DisplayName("[관계자 비밀번호와 전화번호 변경]")
    @Test
    public void setAdminTest() {
        //given
        Admin admin = insertAdmin();

        EditAdminDto dto = EditAdminDto.builder()
                .loginPw("990505soo0300")
                .tel("010-0234-1203")
                .build();

        // when
        Long adminId = adminService.setAdmin(admin.getId(), dto);

        // then
        Optional<Admin> changedAdmin = adminRepository.findById(adminId);
        Assertions.assertThat(changedAdmin.get().getTel()).isEqualTo("010-0234-1203");

    }

    @DisplayName("[관계자 삭제]")
    @Test
    public void removeAdmin() {
        // given
        Admin admin = insertAdmin();
        Long adminId = admin.getId();

        // when
        Long deleteId = adminService.removeAdmin(adminId);

        // then
        Optional<Admin> deleteAdmin = adminRepository.findById(deleteId);
        Assertions.assertThat(deleteAdmin.get().isActive()).isEqualTo(false);

    }

    private Admin insertAdmin() {
        Admin admin = Admin.builder()
                .loginId("admin")
                .loginPw("admin123!")
                .name("관리자")
                .position("10")
                .tel("010-0000-0000")
                .active(true)
                .build();
        return adminRepository.save(admin);
    }
}