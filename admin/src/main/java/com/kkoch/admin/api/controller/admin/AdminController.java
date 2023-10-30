package com.kkoch.admin.api.controller.admin;

import com.kkoch.admin.api.controller.admin.request.AddAdminRequest;
import com.kkoch.admin.api.controller.admin.request.LoginRequest;
import com.kkoch.admin.api.controller.admin.response.AdminResponse;
import com.kkoch.admin.api.service.admin.AdminQueryService;
import com.kkoch.admin.api.service.admin.AdminService;
import com.kkoch.admin.api.service.admin.dto.AddAdminDto;
import com.kkoch.admin.api.service.admin.dto.LoginDto;
import com.kkoch.admin.client.response.MemberResponseForAdmin;
import com.kkoch.admin.login.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/admin-service/intranet")
public class AdminController {

    private final AdminService adminService;
    private final AdminQueryService adminQueryService;

    @GetMapping
    public String index(@ModelAttribute(name = "form") LoginRequest request, @Login LoginAdmin loginAdmin) {
        if (loginAdmin == null) {
            return "login";
        }
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute(name = "form") LoginRequest request, HttpSession session) {
        log.info("<로그인 요청> Controller");
        LoginDto dto = request.toLoginDto();
        LoginAdmin loginAdmin = adminQueryService.loginAdmin(dto);
        session.setAttribute("loginAdmin", loginAdmin);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("<로그아웃 요청> Controller");
        session.invalidate();
        return "login";
    }

    @GetMapping("/admins")
    public String adminPage(@ModelAttribute("form") AddAdminRequest request, Model model) {
        log.info("<관리자 목록 요청> Controller");
        List<AdminResponse> responses = adminQueryService.getAdmins();
        model.addAttribute("admins", responses);
        return "admin";
    }

    @PostMapping("/admins/add")
    public String addAdmin(AddAdminRequest request) {
        log.info("<관리자 등록> Controller. position = {}", request.getPosition());
        AddAdminDto dto = request.toAddAdminDto();
        Long adminId = adminService.addAdmin(dto);
        log.info("admin={}", adminId);
        return "redirect:/admin-service/intranet/admins";
    }

    @GetMapping("/members")
    public String getUsers(Model model) {
        log.info("<회원조회 컨트롤러> Controller");
        List<MemberResponseForAdmin> responses = adminQueryService.getUsers();
        model.addAttribute("members", responses);
        return "member";
    }

}
