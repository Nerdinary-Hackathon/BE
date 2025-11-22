package com.devpath.domain.user.service;

import com.devpath.domain.user.dto.UserProfileRequest;
import com.devpath.domain.user.entity.User;

public interface UserService {
    User createProfile(UserProfileRequest request);
}
