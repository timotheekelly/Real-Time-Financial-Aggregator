package com.mongodb.financialaggregator.model;

import java.util.Date;

public record ExponentialMovingAverageDTO(double ema, Date date) {}
