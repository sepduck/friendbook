package com.friendbook.service;

import com.friendbook.dto.FriendshipDTO;
import com.friendbook.entity.Friendship;

public interface FriendshipService {
    Friendship sendFriendRequest(Long friendId);

    Friendship acceptFriendRequest(Long friendId);

    void deleteFriend(Long friendId);

    boolean canViewPosts(Long ownerId);
}
