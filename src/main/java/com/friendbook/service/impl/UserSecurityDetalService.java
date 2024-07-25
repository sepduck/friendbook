package com.friendbook.service.impl;

import com.friendbook.entity.UserSecurityDetail;
import com.friendbook.entity.Users;
import com.friendbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityDetalService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm kiếm người dùng trong cơ sở dữ liệu bằng username
        Users users = userRepository.findByUsername(username);

        // Nếu không tìm thấy người dùng, ném UsernameNotFoundException
        if (users == null || users.isDeleteUser()) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username);
        }

        // Tạo đối tượng UserSecurityDetail từ thông tin người dùng lấy được
        UserSecurityDetail userSecurityDetail = new UserSecurityDetail(users);

        // Trả về đối tượng UserDetails để Spring Security sử dụng cho quá trình xác thực
        return userSecurityDetail;
    }
}
