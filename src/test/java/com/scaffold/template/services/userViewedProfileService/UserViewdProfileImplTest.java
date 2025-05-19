package com.scaffold.template.services.userViewedProfileService;

import com.scaffold.template.entities.Status;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserViewedProfileEntity;
import com.scaffold.template.repositories.UserViewedProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserViewdProfileImplTest {

    @Mock
    private UserViewedProfileRepository userViewedProfileRepository;

    @Captor
    private ArgumentCaptor<UserViewedProfileEntity> entityCaptor;

    private UserViewedProfileService userViewedProfileService;

    @BeforeEach
    void setUp() {
        userViewedProfileService = new UserViewdProfileImpl(userViewedProfileRepository);
    }

    @Test
    void createUserViewedProfileWithAcceptedStatus() {
        // Arrange
        UserEntity viewer = new UserEntity();
        viewer.setId(1L);
        UserEntity viewedUser = new UserEntity();
        viewedUser.setId(2L);

        // Act
        userViewedProfileService.createUserViewedProfile(viewer, viewedUser, true);

        // Assert
        verify(userViewedProfileRepository).save(entityCaptor.capture());
        UserViewedProfileEntity savedEntity = entityCaptor.getValue();

        assertEquals(viewer, savedEntity.getViewer());
        assertEquals(viewedUser, savedEntity.getViewedUser());
        assertEquals(Status.ACCEPTED, savedEntity.getStatus());
        assertFalse(savedEntity.getIsMatch());
        assertNotNull(savedEntity.getViewedAt());
    }

    @Test
    void createUserViewedProfileWithRejectedStatus() {
        // Arrange
        UserEntity viewer = new UserEntity();
        viewer.setId(1L);
        UserEntity viewedUser = new UserEntity();
        viewedUser.setId(2L);

        // Act
        userViewedProfileService.createUserViewedProfile(viewer, viewedUser, false);

        // Assert
        verify(userViewedProfileRepository).save(entityCaptor.capture());
        UserViewedProfileEntity savedEntity = entityCaptor.getValue();

        assertEquals(Status.REJECTED, savedEntity.getStatus());
    }

    @Test
    void createUserViewedProfileWithNullUsers() {
        // Arrange
        UserEntity viewer = null;
        UserEntity viewedUser = null;

        // Act & Assert
        assertDoesNotThrow(() ->
                userViewedProfileService.createUserViewedProfile(viewer, viewedUser, true)
        );
        verify(userViewedProfileRepository).save(any());
    }

    @Test
    void deleteUserViewedProfileByViewerId() {
        // Arrange
        Long viewerId = 1L;

        // Act
        userViewedProfileService.deleteUserViewedProfileByViewerId(viewerId);

        // Assert
        verify(userViewedProfileRepository).deleteByViewerId(viewerId);
    }

    @Test
    void deleteUserViewedProfileByViewerIdWithNullId() {
        // Act
        userViewedProfileService.deleteUserViewedProfileByViewerId(null);

        // Assert
        verify(userViewedProfileRepository).deleteByViewerId(null);
    }
}