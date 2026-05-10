package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AdminCheckAop {

    private final HttpServletRequest request;
    private final ObjectMapper mapper;

    @Around("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object checkAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        // 요청 시각
        LocalDateTime requestTime = LocalDateTime.now();

        // 유저id, 요청 본문
        Long userId = null;
        String requestBody = null;

        for (Object parameter : joinPoint.getArgs()) {
            if (parameter instanceof Long) {
                userId = (Long) parameter;
            } else {
                requestBody = mapper.writeValueAsString(parameter);
            }
        }

        // 요청 url
        String requestUrl = request.getRequestURI();

        // 실행
        Object result = joinPoint.proceed();

        // 로그 남기기
        log.info("요청 사용자 ID : {} 요청 시각 : {} 요청 URL : {} 요청 본문 : {} 응답 본문 : {}",
                userId,
                requestTime,
                requestUrl,
                requestBody,
                mapper.writeValueAsString(result));

        return result;
    }
}
