package com.three.ott_suggestion.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.JwtUtil;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.global.util.UserDetailsServiceImpl;
import com.three.ott_suggestion.user.entity.RefreshToken;
import com.three.ott_suggestion.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {
            int tokenStatus = jwtUtil.validateToken(tokenValue);

            if (tokenStatus == 1) {
                try {
                    Claims info = jwtUtil.getUserInfoFromExpriedToken(tokenValue);
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                        info.getSubject());
                    RefreshToken refreshToken = refreshTokenRepository.findByUserId(userDetails.getUser().getId());
                    if (refreshToken != null && jwtUtil.validateToken(refreshToken.getRefreshToken()) == 0) {
                        String newAccessToken = jwtUtil.createAccessToken(info.getSubject());
                        res.addHeader(jwtUtil.AUTHORIZATION_HEADER, newAccessToken);

                        res.setStatus(HttpServletResponse.SC_OK);

                        String jsonResponse = objectMapper.writeValueAsString(CommonResponse.<Void>builder().message("새로운 Acces Token이 발급되었습니다.").build());

                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        res.getWriter().write(jsonResponse);
                        return;
                    } else {
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                        String jsonResponse = objectMapper.writeValueAsString(CommonResponse.<Void>builder().message("Access Token과 Refresh Token이 모두 만료되었습니다.").build());
                        refreshTokenRepository.delete(refreshToken);

                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        res.getWriter().write(jsonResponse);
                        return;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            }
            else if (tokenStatus == 2) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, null);
    }
}