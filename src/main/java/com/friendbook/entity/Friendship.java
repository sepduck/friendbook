package com.friendbook.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Long friendshipId;

    // Người gửi yêu cầu kết bạn
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    // Người nhận yêu cầu kết bạn
    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private Users friend;

    // Trạng thái của yêu cầu kết bạn: "PENDING", "ACCEPTED", "DECLINED"
    @Column(name = "status", nullable = false)
    private String status;

    // Ngày tạo yêu cầu kết bạn
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
