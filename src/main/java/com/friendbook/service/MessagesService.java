package com.friendbook.service;

import com.friendbook.dto.MessagesDTO;
import com.friendbook.entity.Messages;

import java.util.List;

public interface MessagesService {
    List<Messages> getMessages(Long friendId);

    Messages saveMessage(long userId, MessagesDTO messagesDTO);

    void deleteMessage(long messageId);
}
