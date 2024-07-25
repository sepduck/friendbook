package com.friendbook.service.impl;

import com.friendbook.dto.CommentsDTO;
import com.friendbook.entity.Comments;
import com.friendbook.entity.Posts;
import com.friendbook.entity.Users;
import com.friendbook.repository.CommentsRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.CommentsService;
import com.friendbook.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsServiceImpl extends BaseService implements CommentsService {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PostsService postsService;
    @Autowired
    private CommentsRepository commentsRepository;

    @Override
    public Comments saveComments(long postId, CommentsDTO commentsDTO) {
        Users users = getCurrentUser(authenticationService);
        Posts posts = postsService.findById(postId);

        Comments comments = new Comments();
        comments.setUsers(users);
        comments.setPosts(posts);
        comments.setContent(commentsDTO.getContent());
        return commentsRepository.save(comments);
    }

    @Override
    public Comments updateComments(long postId, long commentId, CommentsDTO commentsDTO) {
        Comments existingComments = findByCommentId(commentId);
        Users users = getCurrentUser(authenticationService);
        Posts posts = postsService.findById(postId);

        existingComments.setUsers(users);
        existingComments.setPosts(posts);
        existingComments.setContent(commentsDTO.getContent());
        return commentsRepository.save(existingComments);
    }

    @Override
    public void deleteComments(long commentId) {
        commentsRepository.deleteById(commentId);
    }

    @Override
    public List<CommentsDTO> getComments() {
        return List.of();
    }

    @Override
    public Comments findByCommentId(long commentId) {
        return commentsRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
}
