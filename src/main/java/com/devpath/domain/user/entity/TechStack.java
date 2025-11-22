package com.devpath.domain.user.entity;

import com.devpath.domain.user.enums.TechStackName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tech_stacks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id", nullable = false)
    private Long techStackId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tech_stack_name", nullable = false)
    private TechStackName techStackName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
