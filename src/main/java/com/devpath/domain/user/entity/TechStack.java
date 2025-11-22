package com.devpath.domain.user.entity;

import com.devpath.domain.user.enums.TechStackName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tech_stacks", uniqueConstraints = {
        @UniqueConstraint(
                name = "tech_stack_user_unique",
                columnNames = {"user_id", "tech_stack_name"}
        )
})
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
    @Column(name = "tech_stack_name", nullable = false, length = 30)
    private TechStackName techStackName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public static TechStack create(User user, TechStackName stackName) {
        return TechStack.builder()
                .user(user)
                .techStackName(stackName)
                .build();
    }
}
