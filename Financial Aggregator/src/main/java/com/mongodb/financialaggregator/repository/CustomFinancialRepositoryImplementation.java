package com.mongodb.financialaggregator.repository;

import com.mongodb.financialaggregator.model.CompanyDTO;
import com.mongodb.financialaggregator.model.ExponentialMovingAverageDTO;
import com.mongodb.financialaggregator.model.SimpleMovingAverageDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class CustomFinancialRepositoryImplementation implements CustomFinancialRepository {

    private final int EXPONENTIAL_MOVING_AVERAGE_PERIOD = 20;
    private final int SIMPLE_MOVING_AVERAGE_PERIOD = 4;

    private final MongoTemplate mongoTemplate;

    public CustomFinancialRepositoryImplementation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        Aggregation getListOfUniqueCompanies = newAggregation(
                group("company").first("company").as("name"),
                sort(Sort.by("name"))
        );

        return mongoTemplate.aggregate(
                getListOfUniqueCompanies, "Historical_Data", CompanyDTO.class
        ).getMappedResults();
    }


    @Override
    public List<SimpleMovingAverageDTO> getSimpleMovingAverage(String company, Date startDate, Date endDate) {
        Criteria criteria = Criteria
                .where("company").is(company)
                .and("date").gte(startDate).lte(endDate);

        // Define SMA calculation (5-day moving average)
        SetWindowFieldsOperation smaOperation = SetWindowFieldsOperation.builder()
                .partitionBy("company") // Ensures calculations are per company
                .sortBy(Sort.by(Sort.Direction.ASC, "date")) // Sort ascending by Date
                .output(AccumulatorOperators.Avg.avgOf("closeLast")) // Compute SMA using avg()
                .within(SetWindowFieldsOperation.Windows.documents(-SIMPLE_MOVING_AVERAGE_PERIOD, 0)) // Defines a 5-day window (4 previous + current)
                .as("sma") // Output field named "sma"
                .build();

        Aggregation aggregation = newAggregation(
                match(criteria),
                smaOperation,
                project("sma").and("date").as("date") // Keep only SMA and Date fields
        );

        return mongoTemplate.aggregate(
                aggregation, "Historical_Data", SimpleMovingAverageDTO.class
        ).getMappedResults();
    }

    @Override
    public List<ExponentialMovingAverageDTO> getExponentialMovingAverage(String company, Date startDate, Date endDate) {
        Criteria criteria = Criteria
                .where("company").is(company)
                .and("date").gte(startDate).lte(endDate);

        SetWindowFieldsOperation emaOperation = SetWindowFieldsOperation.builder()
                .partitionBy("company")
                .sortBy(Sort.by(Sort.Direction.ASC, "date"))
                .output(AccumulatorOperators.ExpMovingAvg.expMovingAvgOf("closeLast").n(EXPONENTIAL_MOVING_AVERAGE_PERIOD))
                .as("ema")
                .build();

        Aggregation aggregation = newAggregation(
                match(criteria),
                emaOperation,
                project("ema").and("date").as("date")
        );

        return mongoTemplate.aggregate(
                aggregation, "Historical_Data", ExponentialMovingAverageDTO.class
        ).getMappedResults();
    }
}
