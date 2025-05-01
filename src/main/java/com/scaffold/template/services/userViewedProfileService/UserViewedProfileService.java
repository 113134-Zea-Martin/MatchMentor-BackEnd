package com.scaffold.template.services.userViewedProfileService;

import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserViewedProfileService {
    void createUserViewedProfile(UserEntity viewer, UserEntity viewedUser, Boolean status);
    void deleteUserViewedProfileByViewerId(Long viewerId);
}
