package com.friendbook.dto;

import lombok.Data;

@Data
public class CommentsDTO {
    private long postId;
    private String content;
}
