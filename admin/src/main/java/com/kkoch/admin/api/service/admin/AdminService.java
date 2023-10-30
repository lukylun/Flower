package com.kkoch.admin.api.service.admin;

import com.kkoch.admin.api.service.admin.dto.AddAdminDto;
import com.kkoch.admin.api.service.admin.dto.EditAdminDto;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    public Long addAdmin(AddAdminDto dto) {
        Admin admin = dto.toEntity();
        Admin savedAdmin = adminRepository.save(admin);
        return savedAdmin.getId();
    }

    public Long setAdmin(Long adminId, EditAdminDto dto) {
        Admin findAdmin = getAdminEntity(adminId);
        findAdmin.edit(dto.getLoginPw(), dto.getTel());
        return findAdmin.getId();
    }

    public Long removeAdmin(Long adminId) {
        Admin findAdmin = getAdminEntity(adminId);
        findAdmin.remove();
        return findAdmin.getId();
    }

    private Admin getAdminEntity(Long adminId) {
        return adminRepository.findById(adminId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 관계자입니다."));
    }
}

