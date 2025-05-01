package com.scaffold.template.services.userViewedProfileService;

import com.scaffold.template.entities.Status;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserViewedProfileEntity;
import com.scaffold.template.repositories.UserViewedProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserViewdProfileImpl implements UserViewedProfileService {

    private final UserViewedProfileRepository userViewedProfileRepository;

    @Autowired
    public UserViewdProfileImpl(UserViewedProfileRepository userViewedProfileRepository) {
        this.userViewedProfileRepository = userViewedProfileRepository;
    }


    @Override
    public void createUserViewedProfile(UserEntity viewer, UserEntity viewedUser, Boolean status) {
        UserViewedProfileEntity userViewedProfile = new UserViewedProfileEntity();
        userViewedProfile.setViewer(viewer);
        userViewedProfile.setViewedUser(viewedUser);
        userViewedProfile.setStatus(status ? Status.ACCEPTED : Status.REJECTED);
        userViewedProfile.setIsMatch(false); // Assuming isMatch is false by default
        userViewedProfile.setViewedAt(LocalDateTime.now());

        userViewedProfileRepository.save(userViewedProfile);
    }

    @Override
    public void deleteUserViewedProfileByViewerId(Long viewerId) {
        userViewedProfileRepository.deleteByViewerId(viewerId);
    }
}
