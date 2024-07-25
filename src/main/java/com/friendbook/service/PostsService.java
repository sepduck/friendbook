package com.friendbook.service;

import com.friendbook.dto.PostImageDTO;
import com.friendbook.dto.PostVideoDTO;
import com.friendbook.dto.PostsDTO;
import com.friendbook.entity.PostImage;
import com.friendbook.entity.PostVideo;
import com.friendbook.entity.Posts;

import java.util.List;

public interface PostsService {
    List<Posts> getPosts();

    Posts savePost(PostsDTO postsDTO);

    PostImage savePostImage(long postId, PostImageDTO postImageDTO);

    PostVideo savePostVideo(long postId, PostVideoDTO postVideoDTO);

    Posts updatePost(PostsDTO postsDTO);

    Posts findById(long postId);
}
