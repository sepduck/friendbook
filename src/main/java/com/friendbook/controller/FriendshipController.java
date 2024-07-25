package com.friendbook.controller;

import com.friendbook.dto.FriendshipDTO;
import com.friendbook.entity.Friendship;
import com.friendbook.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;

    // API gửi yêu cầu kết bạn
    @PostMapping("/send")
    public ResponseEntity<FriendshipDTO> sendFriendRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.sendFriendRequest(friendshipDTO.getFriendId());
        return ResponseEntity.ok(new FriendshipDTO(friendship.getUser().getUserId(), friendship.getFriend().getUserId(), friendship.getStatus()));
    }

    // API chấp nhận yêu cầu kết bạn
    @PostMapping("/accept")
    public ResponseEntity<FriendshipDTO> acceptFriendRequest(@RequestBody FriendshipDTO friendshipDTO) {
        Friendship friendship = friendshipService.acceptFriendRequest(friendshipDTO.getFriendId());
        return ResponseEntity.ok(new FriendshipDTO(friendship.getUser().getUserId(), friendship.getFriend().getUserId(), friendship.getStatus()));
    }

    // API xóa bạn
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriend(@RequestParam Long friendId) {
        friendshipService.deleteFriend(friendId);
        return ResponseEntity.ok().build();
    }

    // API kiểm tra quyền xem bài viết
    @GetMapping("/can-view")
    public ResponseEntity<Boolean> canViewPosts(@RequestParam Long ownerId) {
        boolean canView = friendshipService.canViewPosts(ownerId);
        return ResponseEntity.ok(canView);
    }
}
