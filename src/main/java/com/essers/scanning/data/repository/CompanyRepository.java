package com.essers.scanning.data.repository;

import com.essers.scanning.data.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    //find all companies with movements
    @Query(value = """
            SELECT * FROM company WHERE id IN
            (SELECT company_id FROM movement where status like 'To Do')""", nativeQuery = true)
    List<Company> findAllWithMovements();
}
