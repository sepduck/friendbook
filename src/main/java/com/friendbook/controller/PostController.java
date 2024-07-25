package com.friendbook.controller;

import com.friendbook.dto.PostImageDTO;
import com.friendbook.dto.PostVideoDTO;
import com.friendbook.dto.PostsDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.entity.PostImage;
import com.friendbook.entity.PostVideo;
import com.friendbook.entity.Posts;
import com.friendbook.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
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

@RestController
@RequestMapping("/api/post")
@Validated
public class PostController extends BaseReponse {
    @Autowired
    private PostsService postsService;

    @GetMapping("/for-user")
    public ResponseEntity<?> getPostsForUser() {
        List<Posts> posts = postsService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping()
    public ResponseEntity<?> savePost(@Valid @RequestBody PostsDTO postsDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages.toString());
            }

            return getResponseEntity(postsService.savePost(postsDTO));
        } catch (Exception e) {
            return getErrorResponseEntity("Error while adding post", 500);
        }
    }

    @PostMapping("/upload-image/{postId}")
    public ResponseEntity<?> uploadImagePost(@PathVariable("postId") long postId,
                                             @ModelAttribute("image") List<MultipartFile> images) {
        try {
            Posts existingPost = postsService.findById(postId);

            images = images == null ? new ArrayList<>() : images;
            if (images.size() > PostImage.MAXIMUM_IMAGE_PER_USER){
                return getErrorResponseEntity("Only one image is allowed", 500);
            }
            List<PostImage> postImages = new ArrayList<>();
            for (MultipartFile image : images) {
                if (image.getSize() == 0){
                    continue;
                }
                if (image.getSize() > 10 * 1024 * 1024){
                    return getErrorResponseEntity("File is too large!", 500);
                }
                String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image!");
                }
                String fileName = storeImage(image);
                PostImage postImage = postsService.savePostImage(
                        existingPost.getPostId(),
                        PostImageDTO.builder()
                                .imageUrl(fileName)
                                .build());
                postImages.add(postImage);
            }
            return getResponseEntity(postImages);
        }catch (Exception e){
            return getErrorResponseEntity("Error while uploading image", 500);
        }
    }
    @PostMapping("/upload-video/{postId}")
    public ResponseEntity<?> uploadVideoPost(@PathVariable("postId") long postId,
                                             @ModelAttribute("videos") List<MultipartFile> videos) {
        try {
            Posts existingPost = postsService.findById(postId);

            videos = videos == null ? new ArrayList<>() : videos;
            if (videos.size() > PostVideo.MAXIMUM_VIDEO_PER_USER){
                return getErrorResponseEntity("Only one video is allowed", 500);
            }
            List<PostVideo> postVideos = new ArrayList<>();
            for (MultipartFile video : videos) {
                if (video.getSize() == 0){
                    continue;
                }
//                if (video.getSize() > 10 * 1024 * 1024){
//                    return getErrorResponseEntity("File is too large!", 500);
//                }
                String contentType = video.getContentType();
                if (contentType == null || !contentType.startsWith("video/") || !contentType.equals("video/mp4")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an video!");
                }
                String fileName = storeImage(video);
                PostVideo postVideo = postsService.savePostVideo(
                        existingPost.getPostId(),
                        PostVideoDTO.builder()
                                .videoUrl(fileName)
                                .build());
                postVideos.add(postVideo);
            }
            return getResponseEntity(postVideos);
        }catch (Exception e){
            return getErrorResponseEntity("Error while uploading image", 500);
        }
    }
    private String storeImage(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + " _ " + fileName;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get("uploads/post");
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

    @PostMapping("/upload-videos/{postId}")
    public ResponseEntity<?> uploadVideoPosts(@PathVariable("postId") long postId,
                                             @ModelAttribute("videos") List<MultipartFile> videos) {
        try {
            Posts existingPost = postsService.findById(postId);

            videos = videos == null ? new ArrayList<>() : videos;
            if (videos.size() > PostVideo.MAXIMUM_VIDEO_PER_USER) {
                return getErrorResponseEntity("Only one video is allowed", 500);
            }

            List<PostVideo> postVideos = new ArrayList<>();
            for (MultipartFile video : videos) {
                if (video.getSize() == 0) {
                    continue;
                }

                String contentType = video.getContentType();
                if (contentType == null || !contentType.equals("video/mp4")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be a video!");
                }

                String fileName = storeVideo(video);
                PostVideo postVideo = postsService.savePostVideo(
                        existingPost.getPostId(),
                        PostVideoDTO.builder()
                                .videoUrl(fileName)
                                .build());
                postVideos.add(postVideo);
            }
            return getResponseEntity(postVideos);
        } catch (Exception e) {
            return getErrorResponseEntity("Error while uploading video", 500);
        }
    }

    private String storeVideo(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("uploads/post");

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }


}
