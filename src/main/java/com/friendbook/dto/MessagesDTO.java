package com.friendbook.dto;

import lombok.Data;

@Data
public class MessagesDTO {
    private long senderId;
    private long receiverId;
    private String content;
}
