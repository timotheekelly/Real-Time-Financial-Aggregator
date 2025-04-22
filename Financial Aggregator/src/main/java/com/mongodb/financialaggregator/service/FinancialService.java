package com.mongodb.financialaggregator.service;

import com.mongodb.financialaggregator.model.*;
import com.mongodb.financialaggregator.repository.CustomFinancialRepository;
import com.mongodb.financialaggregator.repository.FinancialRepository;
import com.mongodb.financialaggregator.repository.LiveDataRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FinancialService {

    private FinancialRepository financialRepository;
    private CustomFinancialRepository customFinancialRepository;
    private LiveDataRepository liveDataRepository;

    public FinancialService(CustomFinancialRepository customFinancialRepository,
                            FinancialRepository financialRepository,
                            LiveDataRepository liveDataRepository) {
        this.customFinancialRepository = customFinancialRepository;
        this.financialRepository = financialRepository;
        this.liveDataRepository = liveDataRepository;
    }

    public List<String> getAllCompanies() {
        List<CompanyDTO> companiesList = customFinancialRepository.getAllCompanies();

        return companiesList.stream()
                .map(CompanyDTO::name)
                .toList();
    }

    public List<StockMarketData> getStockDataBetweenDates(String company, Date startDate, Date endDate) {
        return financialRepository.findAllByCompanyAndDateBetween(company, startDate, endDate);
    }

    public List<SimpleMovingAverageDTO> getSimpleMovingAverage(String companyName, Date startDate, Date endDate) {
        return customFinancialRepository.getSimpleMovingAverage(companyName, startDate, endDate);
    }

    public List<ExponentialMovingAverageDTO> getExponentialMovingAverage(String companyName, Date startDate, Date endDate) {
        return customFinancialRepository.getExponentialMovingAverage(companyName, startDate, endDate);
    }

    public List<LiveStockData> getLiveData() {
        return liveDataRepository.findAll();
    }
}
