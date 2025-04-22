package com.mongodb.financialaggregator.service;

import com.mongodb.financialaggregator.model.LiveStockData;
import com.mongodb.financialaggregator.repository.LiveDataRepository;
import com.mongodb.financialaggregator.utility.LiveStockDataSerde;
import com.mongodb.financialaggregator.utility.RsiCalculator;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RsiStreamProcessor {

    private static final int RSI_PERIOD = 14;
    private final LiveDataRepository liveDataRepository;
    private final Map<String, Deque<Double>> priceHistory = new ConcurrentHashMap<>();

    public RsiStreamProcessor(LiveDataRepository liveDataRepository, StreamsBuilder streamsBuilder) {
        this.liveDataRepository = liveDataRepository;
        buildPipeline(streamsBuilder);
    }

    public void buildPipeline(StreamsBuilder builder) {
        LiveStockDataSerde liveSerde = new LiveStockDataSerde();
        KStream<String, LiveStockData> stream = builder.stream(
                "stock-prices",
                Consumed.with(Serdes.String(), liveSerde)
        );

        stream.peek((key, data) -> {
            System.out.println("Received from Kafka: " + data);

            if (data == null || data.getCloseLast() == null || data.getCompany() == null) {
                return;
            }

            String symbol = data.getCompany();
            double close = data.getCloseLast();

            Deque<Double> prices = priceHistory.computeIfAbsent(symbol, k -> new ArrayDeque<>());
            prices.addLast(close);
            if (prices.size() > RSI_PERIOD + 1) prices.removeFirst();

            if (prices.size() == RSI_PERIOD + 1) {
                double rsi = RsiCalculator.calculate(new ArrayList<>(prices), RSI_PERIOD);
                data.setRsi(rsi);
            }

            liveDataRepository.save(data);
        });
    }
}
