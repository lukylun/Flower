package com.kkoch.admin.config;

import com.kkoch.admin.api.controller.admin.LoginAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
//@Component
public class LoginAdminAuditorAware implements AuditorAware<Long> {

    private final HttpSession httpSession;

    @Override
    public Optional<Long> getCurrentAuditor() {
        LoginAdmin loginAdmin = (LoginAdmin) httpSession.getAttribute("loginAdmin");
        if (loginAdmin == null) {
           return Optional.empty();
        }
        return Optional.ofNullable(loginAdmin.getId());
    }

}
