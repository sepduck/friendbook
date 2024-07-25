package com.friendbook.service;

import com.friendbook.dto.LikeDTO;
import com.friendbook.entity.Likes;

public interface LikeService {
    void likeOrUnlikePost(long postId);
}
