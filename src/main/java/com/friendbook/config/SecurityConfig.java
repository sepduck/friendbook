package com.friendbook.config;

import com.friendbook.service.impl.UserSecurityDetalService;
import com.friendbook.service.jwt.JwtEntryPoint;
import com.friendbook.service.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtEntryPoint entryPoint;
    @Autowired
    private JwtFilter filter;
    @Autowired
    private UserSecurityDetalService userSecurityDetalService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())  // Vô hiệu hóa CSRF và cấu hình CORS với các giá trị mặc định
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/login", "/api/register") // Cho phép truy cập không cần xác thực vào các endpoint /login và /register
                        .permitAll()
                        .anyRequest()
                        .authenticated()) // Yêu cầu xác thực cho tất cả các yêu cầu khác
                .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint)); // Cấu hình xử lý ngoại lệ với JwtEntryPoint
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // Thêm JwtFilter trước UsernamePasswordAuthenticationFilter trong chuỗi bộ lọc
        return http.build();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userSecurityDetalService); // Cấu hình AuthenticationManagerBuilder để sử dụng UserDetailsService
    }
    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // Cung cấp một AuthenticationManager từ AuthenticationConfiguration
    }
    @Bean
    public PasswordEncoder encoder(){
        return NoOpPasswordEncoder.getInstance(); // Cung cấp một PasswordEncoder không mã hóa mật khẩu
    }
}
