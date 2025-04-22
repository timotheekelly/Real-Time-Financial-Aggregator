package com.mongodb.financialaggregator.utility;

import java.util.List;

public class RsiCalculator {

    public static double calculate(List<Double> prices, int period) {
        if (prices.size() < period + 1) return 0.0;

        double gain = 0.0;
        double loss = 0.0;
        for (int i = 1; i <= period; i++) {
            double diff = prices.get(i) - prices.get(i - 1);
            if (diff > 0) gain += diff;
            else loss -= diff;
        }

        if (loss == 0) return 100.0;
        double rs = gain / loss;
        return 100.0 - (100.0 / (1.0 + rs));
    }
}
