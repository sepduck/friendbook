package com.friendbook.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PostsDTO {
    private String content;
    private String image;
    private String video;
}
