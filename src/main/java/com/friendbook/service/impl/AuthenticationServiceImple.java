package com.friendbook.service.impl;

import com.friendbook.dto.UserImageDTO;
import com.friendbook.dto.UsersDTO;
import com.friendbook.entity.*;
import com.friendbook.repository.GenderRepository;
import com.friendbook.repository.RoleRepository;
import com.friendbook.repository.UserImageRepository;
import com.friendbook.repository.UserRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.GenderService;
import com.friendbook.service.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthenticationServiceImple implements AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider provider;
    @Autowired
    private GenderService genderService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserImageRepository userImageRepository;

    private Set<String> invalidatedTokens = new HashSet<>();

    @Override
    public String login(UsersDTO usersDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(usersDTO.getUsername(), usersDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = provider.generateToken(authentication);

        // Lưu trạng thái active vào cơ sở dữ liệu
        Users users = userRepository.findByUsername(usersDTO.getUsername());
        if (users != null) {
            users.setActive(usersDTO.isActive());
            userRepository.save(users);
        }
        return token;
    }

    @Override
    public Users register(UsersDTO usersDTO) {
        Gender gender = genderService.findGenderById(usersDTO.getGenderId());
        Roles roles = roleRepository.findById(usersDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Users users = new Users();
        users.setUsername(usersDTO.getUsername());
        users.setPassword(usersDTO.getPassword());
        users.setLastName(usersDTO.getLastName());
        users.setFirstName(usersDTO.getFirstName());
        users.setPhoneNumber(usersDTO.getPhoneNumber());
        users.setEmail(usersDTO.getEmail());
        users.setBirthday(usersDTO.getBirthday());
        users.setGender(gender);
        users.setActive(usersDTO.isActive());
        users.setRoles(List.of(roles));
        users.setAvatar(usersDTO.getAvatar());
        return userRepository.save(users);
    }

    @Override
    public Users getFindByUserId(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    @Override
    public void setActiveStatus(String username, boolean active) {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            user.setActive(active);
            userRepository.save(user);
        }
    }

    @Override
    public UserImage saveUserImage(long userId,
                                   UserImageDTO userImageDTO) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserImage newUserImage = new UserImage();
        newUserImage.setUsers(existingUser);
        newUserImage.setImageUrl(userImageDTO.getImageUrl());
        UserImageCover newUserImageCover = new UserImageCover();
        newUserImageCover.setImageUrl(userImageDTO.getImageUrl());
        newUserImageCover.setUsers(existingUser);
        int size = userImageRepository.findByUsersUserId(userId).size();
        if (size >= UserImage.MAXIMUM_IMAGE_PER_USER) {
            throw new RuntimeException("Number of images is greater than " + UserImage.MAXIMUM_IMAGE_PER_USER);
        }

        UserImage savedUserImage = userImageRepository.save(newUserImage);

        // Cập nhật trường avatar của Users
        existingUser.setAvatar(savedUserImage.getImageUrl());
        userRepository.save(existingUser);
        return userImageRepository.save(newUserImage);
    }

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }



    @Override
    public boolean softDelete(long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (users != null){
            users.setDeleteUser(true);
            userRepository.save(users);
        }
        return false;
    }

}
