package com.friendbook.repository;

import com.friendbook.entity.PostImage;
import com.friendbook.entity.PostVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVideoRepository extends JpaRepository<PostVideo, Long> {
    List<PostVideo> findByPostsPostId(long postId);
}
