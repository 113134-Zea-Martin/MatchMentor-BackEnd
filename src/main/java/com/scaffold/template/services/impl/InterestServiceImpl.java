package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.interest.InterestRequestDTO;
import com.scaffold.template.entities.InterestEntity;
import com.scaffold.template.repositories.InterestRepository;
import com.scaffold.template.services.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    @Autowired
    public InterestServiceImpl(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }


    @Override
    public List<InterestRequestDTO> getAllInterests() {
        List<InterestRequestDTO> interestRequestDTOs = new ArrayList<>();
        List<InterestEntity> interestEntities = interestRepository.findAll();
        for (InterestEntity interestEntity : interestEntities) {
            InterestRequestDTO interestRequestDTO = new InterestRequestDTO();
            interestRequestDTO.setName(interestEntity.getName());
            interestRequestDTOs.add(interestRequestDTO);
        }
        return interestRequestDTOs;
    }

    @Override
    public InterestEntity createInterest(InterestRequestDTO interestRequestDTO) {
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setName(interestRequestDTO.getName());
        interestEntity.setDescription("Description for " + interestRequestDTO.getName());
        interestEntity.setActive(true);
        interestRepository.save(interestEntity);
        return interestEntity;
    }

    @Override
    public InterestEntity getByName(String name) {
        return interestRepository.findByName(name);
    }
}
