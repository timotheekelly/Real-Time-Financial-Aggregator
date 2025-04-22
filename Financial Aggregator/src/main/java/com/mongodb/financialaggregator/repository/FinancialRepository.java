package com.mongodb.financialaggregator.repository;

import com.mongodb.financialaggregator.model.StockMarketData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface FinancialRepository extends MongoRepository<StockMarketData, ObjectId> {
    List<StockMarketData> findAllByCompanyAndDateBetween(String company, Date startDate, Date endDate);
}
