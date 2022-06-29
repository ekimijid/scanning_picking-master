package com.essers.scanning.data.repository;

import com.essers.scanning.data.model.Picking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickingRepository extends JpaRepository<Picking, Long> {
}
