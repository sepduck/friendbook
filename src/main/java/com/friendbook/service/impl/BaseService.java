package com.friendbook.service.impl;

import com.friendbook.entity.Users;
import com.friendbook.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {
    protected Users getCurrentUser(AuthenticationService authenticationService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return authenticationService.findByUsername(username);
    }
}
