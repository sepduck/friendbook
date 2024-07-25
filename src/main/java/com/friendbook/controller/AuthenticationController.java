package com.friendbook.controller;

import com.friendbook.dto.UserImageDTO;
import com.friendbook.dto.UsersDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.entity.UserImage;
import com.friendbook.entity.Users;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AuthenticationController extends BaseReponse {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtProvider provider;

    private final Path imagePath = Paths.get("uploads/user");

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDTO usersDTO) {
        try {
            usersDTO.setActive(true);
            return getResponseEntity(authenticationService.login(usersDTO));
        }catch (Exception e) {
            return getErrorResponseEntity(e.getMessage(), 500);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsersDTO usersDTO,
                                      BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorsMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessages);
            }
            usersDTO.setRoleId(3);

            return getResponseEntity(authenticationService.register(usersDTO));
        } catch (Exception e) {
            return getErrorResponseEntity(e.getMessage(), 500);
        }
    }

    @PostMapping("user-uploads/{userId}")
    public ResponseEntity<?> uploadImage(@PathVariable("userId") long userId,
                                         @ModelAttribute("file") List<MultipartFile> files) {
        try {
            Users existingUser = authenticationService.getFindByUserId(userId);

            files = files == null ? new ArrayList<>() : files;
            if (files.size() > UserImage.MAXIMUM_IMAGE_PER_USER) {
                return getErrorResponseEntity("Only one file is allowed", 500);
            }
            List<UserImage> userImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large!");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image!");
                }
                String filename = storeFile(file);
                UserImage userImage = authenticationService.saveUserImage(
                        existingUser.getUserId(),
                        UserImageDTO.builder()
                                .imageUrl(filename)
                                .build());
                userImages.add(userImage);
            }
            return ResponseEntity.ok(userImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("user-uploads-cover/{userId}")
    public ResponseEntity<?> uploadImageCover(@PathVariable("userId") long userId,
                                         @ModelAttribute("fileCover") List<MultipartFile> filesCover) {
        try {
            Users existingUser = authenticationService.getFindByUserId(userId);

            filesCover = filesCover == null ? new ArrayList<>() : filesCover;
            if (filesCover.size() > UserImage.MAXIMUM_IMAGE_PER_USER) {
                return getErrorResponseEntity("Only one file is allowed", 500);
            }
            List<UserImage> userImages = new ArrayList<>();
            for (MultipartFile file : filesCover) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large!");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image!");
                }
                String filename = storeFile(file);
                UserImage userImage = authenticationService.saveUserImage(
                        existingUser.getUserId(),
                        UserImageDTO.builder()
                                .imageUrl(filename)
                                .build());
                userImages.add(userImage);
            }
            return ResponseEntity.ok(userImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + " _ " + fileName;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads/user");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

        // Sao chép file vào thư
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable long id) {
        try {
            Users users = authenticationService.getFindByUserId(id);
            return getResponseEntity(users);
        } catch (Exception e) {
            return getErrorResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND.value());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {

            String authenticationHeader = request.getHeader("Authorization");
            if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
                return getErrorResponseEntity("Thiếu hoặc sai định dạng Authorization header", 500);
            }

            String token = authenticationHeader.replace("Bearer ", "");
            String username = provider.getUsernameFormToken(token);

            if (username != null) {
                authenticationService.setActiveStatus(username, false);
                authenticationService.invalidateToken(token);
                return getResponseEntity("Đăng xuất thành công!");
            } else {
                return getErrorResponseEntity("Không thể xác định người dùng từ token", 500);
            }
        }catch (Exception e) {
            return getErrorResponseEntity("Có lỗi xảy ra trong quá trình đăng xuất", 500);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> viewUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Users users = authenticationService.findByUsername(username);

        if (users != null) {
            return getResponseEntity(users);
        }else {
            return getErrorResponseEntity("Người dùng không tồn tại", 500);
        }
    }


    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
            Path imagePath = Paths.get("uploads/user/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/images/{imageName:.+}")
    public ResponseEntity<?> viewImage1(@PathVariable String imageName) {
        try {
            Path imagePath = this.imagePath.resolve(imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && Files.isReadable(imagePath)) {
                MediaType mediaType = MediaType.IMAGE_JPEG; // Kiểm tra loại tệp hình ảnh
                if (imageName.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else if (imageName.endsWith(".gif")) {
                    mediaType = MediaType.IMAGE_GIF;
                }

                return ResponseEntity.ok().contentType(mediaType).body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load image: " + e.getMessage());
        }
    }

}
