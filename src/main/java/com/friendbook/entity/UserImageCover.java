package com.friendbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserImageCover {
    public static final int MAXIMUM_IMAGE_PER_USER = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCoverId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    private String imageUrl;
}
