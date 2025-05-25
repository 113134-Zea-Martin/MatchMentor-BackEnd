package com.scaffold.template.services.userActivity;

import com.scaffold.template.entities.UserActivityEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface UserActivityService {
    void createUserActivity(Long userId, String activityType);
    List<UserActivityEntity> getUserActivitiesbyDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
