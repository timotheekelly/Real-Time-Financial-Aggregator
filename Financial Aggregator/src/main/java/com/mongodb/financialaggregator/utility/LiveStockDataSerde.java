package com.mongodb.financialaggregator.utility;

import com.mongodb.financialaggregator.model.LiveStockData;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class LiveStockDataSerde extends Serdes.WrapperSerde<LiveStockData> {
    public LiveStockDataSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(LiveStockData.class));
    }
}
