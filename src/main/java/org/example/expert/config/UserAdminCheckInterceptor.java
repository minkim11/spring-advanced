package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
public class UserAdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 로그인한 유저 Role 꺼내기
        UserRole role = UserRole.valueOf((String) request.getAttribute("userRole"));

        // ADMIN인지 확인하기
        if (!(role == UserRole.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 삭제할 수 있습니다.");
            return false;
        }
        log.info("요청 시각: {} 요청 URL: {}", LocalDateTime.now(), request.getRequestURI());
        return true;
    }


}
