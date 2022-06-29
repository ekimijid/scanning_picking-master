package com.essers.scanning.data.repository;

import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    //    Find first movement by movement_type.id and company.id with highest priority
    @Query(value = """
            SELECT * FROM movement m WHERE m.movement_type_id = :movementTypeId 
            AND m.company_id = :companyId AND m.status = 'To Do' 
            ORDER BY m.priority DESC LIMIT 1""", nativeQuery = true)
    Movement findNextTodoTaskForActiveCompany(@Param("movementTypeId") Long movementTypeId,
                                              @Param("companyId") Long companyId);

    Movement findFirstByUserAndStatus(User user, String status);
}
