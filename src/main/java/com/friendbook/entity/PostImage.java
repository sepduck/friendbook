package com.friendbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PostImage {
    public static final int MAXIMUM_IMAGE_PER_USER = 20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    private String imageUrl;
}
