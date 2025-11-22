package com.devpath.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowId implements Serializable {
    @Column(name = "follower_id")
    private Long userId;

    @Column(name = "following_id")
    private Long followerId;
}
