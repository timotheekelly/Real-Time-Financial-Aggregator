package com.mongodb.financialaggregator.repository;

import com.mongodb.financialaggregator.model.LiveStockData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LiveDataRepository extends MongoRepository<LiveStockData, ObjectId> {
}

