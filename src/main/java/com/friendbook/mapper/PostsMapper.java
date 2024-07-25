package com.friendbook.mapper;

import com.friendbook.dto.PostsDTO;
import com.friendbook.entity.Posts;

public class PostsMapper {
    public static PostsDTO toDto(Posts posts) {
        return PostsDTO.builder()
                .content(posts.getContent())
                .build();
    }
}
