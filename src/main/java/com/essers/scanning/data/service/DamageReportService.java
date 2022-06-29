package com.essers.scanning.data.service;

import com.essers.scanning.data.model.DamageReport;
import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.repository.DamageReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class DamageReportService {
    private final DamageReportRepository damageReportRepository;

    public DamageReportService(DamageReportRepository damageReportRepository) {
        this.damageReportRepository = damageReportRepository;
    }

    public List<DamageReport> findAll() {
        return damageReportRepository.findAll();
    }

    public List<DamageReport> getAllDamagesOfMovement(Movement movement) {
        return damageReportRepository.findAllByMovement(movement);
    }

    public void save(DamageReport damageReport) {
        damageReportRepository.save(damageReport);
    }
}
