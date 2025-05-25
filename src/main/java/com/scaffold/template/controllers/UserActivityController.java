package com.scaffold.template.controllers;

import com.scaffold.template.services.userActivity.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-activity")
public class UserActivityController {

    private final UserActivityService userActivityService;

    @Autowired
    public UserActivityController(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    @PostMapping()
    public boolean saveUserActivity(@RequestParam Long userId, @RequestParam String activityId) {
        try {
            userActivityService.createUserActivity(userId, activityId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
