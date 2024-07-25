package com.friendbook.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserImage {
    public static final int MAXIMUM_IMAGE_PER_USER = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    private String imageUrl;
}
