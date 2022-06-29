package com.essers.scanning.data.service;

import com.essers.scanning.data.model.MovementType;
import com.essers.scanning.data.repository.MovementTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class MovementTypeService {
    private final MovementTypeRepository movementTypeRepository;

    public MovementTypeService(MovementTypeRepository movementTypeRepository) {
        this.movementTypeRepository = movementTypeRepository;
    }

    public List<MovementType> findAll() {
        return movementTypeRepository.findAll();
    }
}
