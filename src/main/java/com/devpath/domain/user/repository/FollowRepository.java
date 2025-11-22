package com.devpath.domain.user.repository;

import com.devpath.domain.user.entity.Follow;
import com.devpath.domain.user.entity.User;
import com.devpath.domain.user.enums.JobGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("""
    SELECT f FROM Follow f
    WHERE f.user.id = :userId
    AND (:cursor IS NULL OR f.id < :cursor)
    AND (:jobGroup IS NULL OR f.follower.jobGroup = :jobGroup)
    ORDER BY f.id DESC
    """)
    Slice<Follow> findNextByCursor(Long userId, Long cursor, JobGroup jobGroup, Pageable pageable);


    boolean existsByUser_IdAndFollower_Id(Long userId, Long followerId);

}
