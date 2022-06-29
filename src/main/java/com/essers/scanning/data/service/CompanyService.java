package com.essers.scanning.data.service;

import com.essers.scanning.data.model.Company;
import com.essers.scanning.data.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAllCompaniesWithMovements() {
        return companyRepository.findAllWithMovements();
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

}
