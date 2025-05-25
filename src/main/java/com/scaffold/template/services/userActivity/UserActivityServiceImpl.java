package com.scaffold.template.services.userActivity;

import com.scaffold.template.entities.UserActivityEntity;
import com.scaffold.template.repositories.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

    @Autowired
    public UserActivityServiceImpl(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    @Override
    public void createUserActivity(Long userId, String activityType) {
        // Create a new UserActivityEntity object
        UserActivityEntity userActivity = new UserActivityEntity();
        userActivity.setUserId(userId);
        userActivity.setActivityType(activityType);
        userActivity.setActivityDate(LocalDateTime.now());

        // Save the activity to the database
        userActivityRepository.save(userActivity);
    }

    @Override
    public List<UserActivityEntity> getUserActivitiesbyDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime for the start and end dates
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Fetch user activities from the repository
        return userActivityRepository.findAllByActivityDateBetween(startDateTime, endDateTime);
    }
}
