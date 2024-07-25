package com.friendbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class GroupPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_post_id")
    private Long groupPostId;

    // Liên kết với người dùng đăng bài viết
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // Liên kết với nhóm mà bài viết thuộc về
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;

    @Column(name = "content")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name = "video")
    private String video;

    // Cờ để xác định bài viết đã được duyệt hay chưa
    @Column(name = "approved")
    private boolean approved = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Thiết lập thời gian tạo bài viết
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Thiết lập thời gian cập nhật bài viết
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
