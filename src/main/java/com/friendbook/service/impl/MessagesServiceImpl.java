package com.friendbook.service.impl;

import com.friendbook.dto.MessagesDTO;
import com.friendbook.entity.Friendship;
import com.friendbook.entity.Messages;
import com.friendbook.entity.Users;
import com.friendbook.repository.FriendshipRepository;
import com.friendbook.repository.MessagesRepository;
import com.friendbook.service.AuthenticationService;
import com.friendbook.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessagesServiceImpl extends BaseService implements MessagesService {
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Override
    public List<Messages> getMessages(Long friendId) {
        Users currentUser = getCurrentUser(authenticationService);
        Users friend = authenticationService.getFindByUserId(friendId);

        // Kiểm tra xem hai người dùng đã kết bạn hay chưa
        boolean areFriends = areUsersFriends(currentUser.getUserId(), friendId);

        if (!areFriends) {
            throw new IllegalStateException("Bạn chỉ có thể xem tin nhắn khi đã kết bạn.");
        }

        return messagesRepository.findMessagesBetweenUsers(currentUser.getUserId(), friendId);
    }

    @Override
    public Messages saveMessage(long userId, MessagesDTO messagesDTO) {
        Users sender = getCurrentUser(authenticationService);
        Users receiver = authenticationService.getFindByUserId(userId);

        // Kiểm tra xem hai người dùng đã kết bạn hay chưa
        boolean areFriends = areUsersFriends(sender.getUserId(), receiver.getUserId());

        if (!areFriends) {
            throw new IllegalStateException("Bạn chỉ có thể nhắn tin khi đã kết bạn.");
        }

        Messages messages = new Messages();
        messages.setSenderId(sender);
        messages.setReceiverId(receiver);
        messages.setContent(messagesDTO.getContent());
        return messagesRepository.save(messages);
    }

    @Override
    public void deleteMessage(long messageId) {
        messagesRepository.deleteById(messageId);
    }

    private boolean areUsersFriends(Long userId, Long friendId) {
        Optional<Friendship> friendship = friendshipRepository.findByUserUserIdAndFriendUserIdAndStatus(userId, friendId, "ACCEPTED");
        if (!friendship.isPresent()) {
            friendship = friendshipRepository.findByUserUserIdAndFriendUserIdAndStatus(friendId, userId, "ACCEPTED");
        }
        return friendship.isPresent();
    }
}
