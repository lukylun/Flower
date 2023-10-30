package com.kkoch.admin.api.controller.admin;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.admin.request.EditAdminRequest;
import com.kkoch.admin.api.service.admin.AdminQueryService;
import com.kkoch.admin.api.service.admin.AdminService;
import com.kkoch.admin.api.service.admin.dto.EditAdminDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin-service/admins")
@Slf4j
public class AdminApiController {

    private final AdminService adminService;
    private final AdminQueryService adminQueryService;

    //관게자 수정
    @PatchMapping("/{adminId}")
    public ApiResponse<Long> setAdmin(@PathVariable Long adminId, @RequestBody EditAdminRequest request) {
        log.info("<관계자 정보 수정> Controller");
        EditAdminDto dto = request.toEditAdminDto();
        Long setId = adminService.setAdmin(adminId, dto);
        return ApiResponse.ok(setId);

    }

    //관계자 삭제
    @DeleteMapping("/{adminId}")
    public ApiResponse<Long> removeAdmin(@PathVariable Long adminId) {
        log.info("<관계자 삭제> Controller");
        Long deleteId = adminService.removeAdmin(adminId);
        return ApiResponse.of(MOVED_PERMANENTLY, "관계자 정보가 삭제되었습니다.", deleteId);
    }
}
