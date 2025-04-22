package com.mongodb.financialaggregator.repository;

import com.mongodb.financialaggregator.model.CompanyDTO;
import com.mongodb.financialaggregator.model.ExponentialMovingAverageDTO;
import com.mongodb.financialaggregator.model.SimpleMovingAverageDTO;

import java.util.Date;
import java.util.List;

public interface CustomFinancialRepository {
    List<CompanyDTO> getAllCompanies();
    List<SimpleMovingAverageDTO> getSimpleMovingAverage(String company, Date startDate, Date endDate);
    List<ExponentialMovingAverageDTO> getExponentialMovingAverage(String company, Date startDate, Date endDate);
}
