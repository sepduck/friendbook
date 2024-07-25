package com.friendbook.controller;

import com.friendbook.entity.BaseReponse;
import com.friendbook.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController extends BaseReponse {
    @Autowired
    private AuthenticationService authenticationService;

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") long userId) {
        try {
            authenticationService.softDelete(userId);
            return getResponseEntity("User deleted successfully");
        }catch (Exception e) {
            return getErrorResponseEntity("Error while deleting user", 500);
        }

    }

}
