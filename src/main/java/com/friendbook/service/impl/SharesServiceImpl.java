package com.friendbook.service.impl;

import com.friendbook.dto.SharesDTO;
import com.friendbook.entity.Posts;
import com.friendbook.entity.Shares;
import com.friendbook.entity.Users;
import com.friendbook.repository.SharesRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.PostsService;
import com.friendbook.service.SharesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SharesServiceImpl extends BaseService implements SharesService {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SharesRepository sharesRepository;
    @Autowired
    private PostsService postsService;

    @Override
    public Shares saveShare(long postId, SharesDTO sharesDTO) {
        Users users = getCurrentUser(authenticationService);
        Posts posts = postsService.findById(postId);
        Shares shares = new Shares();
        shares.setUsers(users);
        shares.setPosts(posts);
        shares.setContent(sharesDTO.getContent());
        return sharesRepository.save(shares);
    }

    @Override
    public Shares updateShare(long postId, long shareId, SharesDTO sharesDTO) {
        Shares existingShares = findByShareId(shareId);
        Users users = getCurrentUser(authenticationService);
        Posts posts = postsService.findById(postId);

        existingShares.setUsers(users);
        existingShares.setPosts(posts);
        existingShares.setContent(sharesDTO.getContent());
        return sharesRepository.save(existingShares);
    }

    @Override
    public void deleteShare(long shareId) {
        sharesRepository.deleteById(shareId);
    }

    @Override
    public Shares findByShareId(long shareId) {
        return sharesRepository.findById(shareId)
                .orElseThrow(() -> new RuntimeException("Share not found"));
    }
}
