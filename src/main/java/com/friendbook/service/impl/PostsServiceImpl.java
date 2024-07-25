package com.friendbook.service.impl;

import com.friendbook.dto.PostImageDTO;
import com.friendbook.dto.PostVideoDTO;
import com.friendbook.dto.PostsDTO;
import com.friendbook.entity.*;
import com.friendbook.repository.FriendshipRepository;
import com.friendbook.repository.PostImageRepository;
import com.friendbook.repository.PostVideoRepository;
import com.friendbook.repository.PostsRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostsServiceImpl extends BaseService implements PostsService {
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private PostVideoRepository postVideoRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Override
    public List<Posts> getPosts() {
        Users users = getCurrentUser(authenticationService); // Lấy người dùng hiện tại
        List<Posts> allPosts = postsRepository.findAll(); // Lấy tất cả bài đăng

        // Lọc danh sách bài đăng mà người dùng hiện tại có thể xem
        return allPosts.stream()
                .filter(post -> canViewPost(post, users))
                .collect(Collectors.toList());
    }

    @Override
    public Posts savePost(PostsDTO postsDTO) {
        Users users = getCurrentUser(authenticationService);

        Posts posts = new Posts();
        posts.setUsers(users);
        posts.setContent(postsDTO.getContent());
        posts.setImage(postsDTO.getImage());
        posts.setVideo(postsDTO.getVideo());
        return postsRepository.save(posts);
    }

    @Override
    public PostImage savePostImage(long postId,
                                   PostImageDTO postImageDTO) {
        Posts existingPost = findById(postId);
        PostImage newPostImage = new PostImage();
        newPostImage.setPosts(existingPost);
        newPostImage.setImageUrl(postImageDTO.getImageUrl());
        int size = postImageRepository.findByPostsPostId(postId).size();
        if (size > PostImage.MAXIMUM_IMAGE_PER_USER) {
            throw new RuntimeException("Maximum image per user");
        }

        PostImage savedPostImage = postImageRepository.save(newPostImage);

        existingPost.setImage(savedPostImage.getImageUrl());
        postsRepository.save(existingPost);
        return postImageRepository.save(savedPostImage);
    }

    @Override
    public PostVideo savePostVideo(long postId,
                                   PostVideoDTO postVideoDTO) {
        Posts existingPost = findById(postId);
        PostVideo newPostVideo = new PostVideo();
        newPostVideo.setPosts(existingPost);
        newPostVideo.setVideoUrl(postVideoDTO.getVideoUrl());
        int size = postVideoRepository.findByPostsPostId(postId).size();
        if (size > PostVideo.MAXIMUM_VIDEO_PER_USER) {
            throw new RuntimeException("Maximum video per user");
        }

        PostVideo savedPostVideo = postVideoRepository.save(newPostVideo);

        existingPost.setVideo(savedPostVideo.getVideoUrl());
        postsRepository.save(existingPost);
        return postVideoRepository.save(savedPostVideo);
    }

    @Override
    public Posts updatePost(PostsDTO postsDTO) {
        return null;
    }

    @Override
    public Posts findById(long postId) {
        return postsRepository.findById(postId).
                orElseThrow(() -> new RuntimeException("Post not found"));
    }

    private boolean canViewPost(Posts post, Users currentUser) {
        Users postOwner = post.getUsers(); // Lấy người dùng đăng bài

        // Kiểm tra null cho postOwner và currentUser
        if (postOwner == null || currentUser == null) {
            return false;
        }

        // Người đăng bài có thể xem bài viết của mình
        if (postOwner.getUserId().equals(currentUser.getUserId())) {
            return true;
        }

        // Kiểm tra xem người dùng hiện tại có phải là bạn của người đăng bài không
        Optional<Friendship> friendship = friendshipRepository
                .findByUserUserIdAndFriendUserIdAndStatus(postOwner.getUserId(),
                        currentUser.getUserId(),
                        "ACCEPTED");

        // Kiểm tra quan hệ bạn bè ngược lại
        if (!friendship.isPresent()) {
            friendship = friendshipRepository.findByUserUserIdAndFriendUserIdAndStatus(
                    currentUser.getUserId(), postOwner.getUserId(), "ACCEPTED");
        }

        return friendship.isPresent();
    }
}
