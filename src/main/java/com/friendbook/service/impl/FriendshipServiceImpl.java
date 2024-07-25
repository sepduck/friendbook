package com.friendbook.service.impl;

import com.friendbook.dto.FriendshipDTO;
import com.friendbook.entity.Friendship;
import com.friendbook.entity.Users;
import com.friendbook.repository.FriendshipRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendshipServiceImpl extends BaseService implements FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private AuthenticationService authenticationService;

    // Gửi yêu cầu kết bạn
    public Friendship sendFriendRequest(Long friendId) {
        Users user = getCurrentUser(authenticationService);
        Users friend = authenticationService.getFindByUserId(friendId);

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setStatus("PENDING");

        return friendshipRepository.save(friendship);
    }

    // Chấp nhận yêu cầu kết bạn
    public Friendship acceptFriendRequest(Long friendId) {
        Users user = getCurrentUser(authenticationService);
        Optional<Friendship> friendship = friendshipRepository.findByUserUserIdAndFriendUserId(user.getUserId(), friendId);
        if (friendship.isPresent()) {
            friendship.get().setStatus("ACCEPTED");
            return friendshipRepository.save(friendship.get());
        } else {
            throw new RuntimeException("Friend request not found");
        }
    }

    // Xóa bạn
    public void deleteFriend(Long friendId) {
        Users user = getCurrentUser(authenticationService);
        Optional<Friendship> friendship = friendshipRepository.findByUserUserIdAndFriendUserId(user.getUserId(), friendId);
        friendship.ifPresent(friendshipRepository::delete);
    }

    // Kiểm tra quyền xem bài viết
    public boolean canViewPosts(Long ownerId) {
        Users user = getCurrentUser(authenticationService);
        if (user.getUserId().equals(ownerId)) {
            return true; // Người dùng có thể xem bài viết của chính họ
        }

        Optional<Friendship> friendship = friendshipRepository.findByUserUserIdAndFriendUserIdAndStatus(ownerId, user.getUserId(), "ACCEPTED");
        return friendship.isPresent(); // Kiểm tra xem người dùng có phải là bạn của người sở hữu bài viết hay không
    }
}
