package com.devpath.domain.user.repository;

import com.devpath.domain.user.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
}

