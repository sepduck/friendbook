package com.friendbook.service.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider provider;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");  // Lấy token từ header "Authorization"
        if (token != null) {
            token = token.replace("Bearer", "").trim(); // Loại bỏ chuỗi "Bearer" và khoảng trắng nếu có
            if (provider.validateToken(token)) { // Kiểm tra tính hợp lệ của token
                String username = provider.getUsernameFormToken(token); // Lấy tên người dùng từ token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Lấy thông tin người dùng
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // Tạo đối tượng xác thực
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Thiết lập đối tượng xác thực vào SecurityContext
            }
        }
        filterChain.doFilter(request, response); // Tiếp tục thực hiện các bộ lọc khác trong chuỗi
    }
}
