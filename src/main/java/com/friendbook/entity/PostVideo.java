package com.friendbook.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PostVideo {
    public static final int MAXIMUM_VIDEO_PER_USER = 20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postVideoId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    private String videoUrl;

    private String videoType;
}
