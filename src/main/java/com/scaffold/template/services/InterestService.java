package com.scaffold.template.services;

import com.scaffold.template.dtos.interest.InterestRequestDTO;
import com.scaffold.template.entities.InterestEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InterestService {
     List<InterestRequestDTO> getAllInterests();
     InterestEntity createInterest(InterestRequestDTO interestRequestDTO);
     InterestEntity getByName(String name);
}
