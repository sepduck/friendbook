package com.friendbook.controller;

import com.friendbook.dto.MessagesDTO;
import com.friendbook.entity.BaseReponse;
import com.friendbook.entity.Messages;
import com.friendbook.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessagesController extends BaseReponse {
    @Autowired
    private MessagesService messagesService;

    @GetMapping("/{friendId}")
    public ResponseEntity<?> getMessages(@PathVariable("friendId") Long friendId) {
        try {
            List<Messages> messages = messagesService.getMessages(friendId);
            return ResponseEntity.ok(messages);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> sendMessage(@PathVariable("userId") int userId, @RequestBody MessagesDTO messagesDTO) {
        try {
            Messages messages = messagesService.saveMessage(userId, messagesDTO);
            return getResponseEntity(messages);
        }catch (Exception e) {
            return getErrorResponseEntity("Error while adding messages", 500);
        }
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable("messageId") int messageId) {
        try {
            messagesService.deleteMessage(messageId);
            return getResponseEntity("Message deleted successfully");
        }catch (Exception e) {
            return getErrorResponseEntity("Error while deleting message", 500);
        }
    }
}
