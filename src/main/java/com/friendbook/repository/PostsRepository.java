package com.friendbook.repository;

import com.friendbook.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

//    // Tìm kiếm bài đăng mà người dùng có thể xem
//    List<Posts> findByUsersUserId(Long userId);
}
