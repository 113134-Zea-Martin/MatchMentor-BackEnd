package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.interest.InterestRequestDTO;
import com.scaffold.template.entities.InterestEntity;
import com.scaffold.template.repositories.InterestRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InterestServiceImplTest {
    @Test
    void getAllInterestsReturnsEmptyListWhenNoInterestsExist() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);
        when(mockInterestRepository.findAll()).thenReturn(new ArrayList<>());

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        List<InterestRequestDTO> result = interestService.getAllInterests();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllInterestsReturnsListOfInterestsWhenInterestsExist() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);

        InterestEntity interest1 = new InterestEntity();
        interest1.setName("Interest1");
        InterestEntity interest2 = new InterestEntity();
        interest2.setName("Interest2");

        when(mockInterestRepository.findAll()).thenReturn(List.of(interest1, interest2));

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        List<InterestRequestDTO> result = interestService.getAllInterests();

        assertEquals(2, result.size());
        assertEquals("Interest1", result.get(0).getName());
        assertEquals("Interest2", result.get(1).getName());
    }

    @Test
    void createInterestSavesAndReturnsNewInterest() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);

        InterestRequestDTO request = new InterestRequestDTO();
        request.setName("NewInterest");

        InterestEntity savedEntity = new InterestEntity();
        savedEntity.setName("NewInterest");
        savedEntity.setDescription("Description for NewInterest");
        savedEntity.setActive(true);

        when(mockInterestRepository.save(any(InterestEntity.class))).thenReturn(savedEntity);

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        InterestEntity result = interestService.createInterest(request);

        assertNotNull(result);
        assertEquals("NewInterest", result.getName());
        assertEquals("Description for NewInterest", result.getDescription());
        assertTrue(result.isActive());
    }

    @Test
    void getByNameReturnsInterestWhenNameExists() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);

        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setName("ExistingInterest");

        when(mockInterestRepository.findByName("ExistingInterest")).thenReturn(interestEntity);

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        InterestEntity result = interestService.getByName("ExistingInterest");

        assertNotNull(result);
        assertEquals("ExistingInterest", result.getName());
    }

    @Test
    void getByNameReturnsNullWhenNameDoesNotExist() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);

        when(mockInterestRepository.findByName("NonexistentInterest")).thenReturn(null);

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        InterestEntity result = interestService.getByName("NonexistentInterest");

        assertNull(result);
    }

    @Test
    void getByIdReturnsInterestWhenIdExists() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);

        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setId(1L);

        when(mockInterestRepository.findById(1L)).thenReturn(java.util.Optional.of(interestEntity));

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        InterestEntity result = interestService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getByIdReturnsNullWhenIdDoesNotExist() {
        InterestRepository mockInterestRepository = mock(InterestRepository.class);

        when(mockInterestRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        InterestServiceImpl interestService = new InterestServiceImpl(mockInterestRepository);

        InterestEntity result = interestService.getById(99L);

        assertNull(result);
    }
}