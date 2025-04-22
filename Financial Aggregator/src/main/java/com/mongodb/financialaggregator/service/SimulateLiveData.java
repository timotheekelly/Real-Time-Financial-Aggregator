package com.mongodb.financialaggregator.service;

import com.mongodb.financialaggregator.model.LiveStockData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.Date;
import java.util.Random;

@Service
public class SimulateLiveData {

    private Date currentSimulatedDate;
    private double previousClose;
    private final KafkaTemplate<String, LiveStockData> kafkaTemplate;

    private static final String COMPANY = "DOOF";
    private static final double INITIAL_OPEN = 100.0;
    private static final double INITIAL_VOLUME = 1000.0;

    private final double trendSlope = 0.002;
    private boolean trendingUp = true;
    private int trendCounter = 0;

    private final int oscillationPeriod = 30;
    private final double oscillationAmplitude = 1.5;

    private final Random random = new Random();

    public SimulateLiveData(KafkaTemplate<String, LiveStockData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.currentSimulatedDate = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());
        this.previousClose = INITIAL_OPEN;
    }

    public LiveStockData simulateLiveData() {
        LiveStockData stock = new LiveStockData();

        stock.setDate(currentSimulatedDate);
        stock.setCompany(COMPANY);

        double trendAdjustment = trendingUp ? trendSlope : -trendSlope;
        double oscillation = oscillationAmplitude *
                Math.sin((2 * Math.PI * trendCounter) / oscillationPeriod);
        double marketBaseline = previousClose * (1 + trendAdjustment) + oscillation;

        boolean hasVolatility = random.nextDouble() < 0.1;
        double volatilityBoost = hasVolatility ? (randomBetween(-0.1, 0.1)) : 0;

        double open = marketBaseline * (1 + volatilityBoost);
        double close = open * (1 + randomBetween(-0.05, 0.05));
        double high = Math.max(open, close) * (1 + randomBetween(0.01, 0.1));
        double low = Math.min(open, close) * (1 - randomBetween(0.01, 0.1));
        double volume = INITIAL_VOLUME * (1 + randomBetween(-0.5, 0.5));

        stock.setOpen(round(open));
        stock.setCloseLast(round(close));
        stock.setHigh(round(high));
        stock.setLow(round(low));
        stock.setVolume(round(volume));

        previousClose = close;
        trendCounter++;

        // Increment currentSimulatedDate by 1 day
        LocalDate next = currentSimulatedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .plusDays(1);
        currentSimulatedDate = Date.from(next.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Flip trend every 60 days
        if (trendCounter % 60 == 0) {
            trendingUp = !trendingUp;
        }

        return stock;
    }

    @Scheduled(fixedRate = 1000)
    public void simulateEverySecond() {
        LiveStockData data = simulateLiveData();

        System.out.println("========== STOCK DEBUG INFO ==========");
        System.out.println("Company:       " + data.getCompany());
        System.out.println("Date (raw):    " + data.getDate());
        System.out.println("Date (class):  " + (data.getDate() != null ? data.getDate().getClass().getName() : "null"));
        System.out.println("Open:          " + data.getOpen());
        System.out.println("Close/Last:    " + data.getCloseLast());
        System.out.println("High:          " + data.getHigh());
        System.out.println("Low:           " + data.getLow());
        System.out.println("Volume:        " + data.getVolume());
        System.out.println("======================================");

        try {
            kafkaTemplate.send("stock-prices", data.getCompany(), data);
            System.out.println("Sent successfully.\n");
        } catch (Exception e) {
            System.err.println("Insert failed: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private double randomBetween(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
