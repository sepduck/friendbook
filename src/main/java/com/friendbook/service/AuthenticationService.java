package com.friendbook.service;

import com.friendbook.dto.UserImageDTO;
import com.friendbook.dto.UsersDTO;
import com.friendbook.entity.UserImage;
import com.friendbook.entity.Users;

public interface AuthenticationService {
    String login(UsersDTO usersDTO);

    Users register(UsersDTO usersDTO);

    Users getFindByUserId(long id);

    void setActiveStatus(String username, boolean active);


    void invalidateToken(String token);

    UserImage saveUserImage(long userId, UserImageDTO userImageDTO);

    Users findByUsername(String username);

    boolean softDelete(long userId);
}
