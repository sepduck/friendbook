package com.friendbook.service.impl;

import com.friendbook.dto.LikeDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.entity.Likes;
import com.friendbook.entity.Posts;
import com.friendbook.entity.Users;
import com.friendbook.repository.LikesRepository;
import com.friendbook.repository.PostsRepository;
import com.friendbook.repository.UserRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.LikeService;
import com.friendbook.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl extends BaseService implements LikeService {
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private PostsService postsService;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void likeOrUnlikePost(long postId) {
        Users users = getCurrentUser(authenticationService);
        Posts posts = postsService.findById(postId);

        Optional<Likes> existingLikes = likesRepository.findByUsersAndPosts(users, posts);
        if (existingLikes.isPresent()) {
            likesRepository.delete(existingLikes.get());
        }else {
            Likes likes = new Likes();
            likes.setPosts(posts);
            likes.setUsers(users);
            likesRepository.save(likes);
        }
    }
}
