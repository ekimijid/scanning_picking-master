package com.essers.scanning.data.repository;

import com.essers.scanning.data.model.DamageReport;
import com.essers.scanning.data.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamageReportRepository extends JpaRepository<DamageReport, Long> {
    List<DamageReport> findAllByMovement(Movement movement);
}
