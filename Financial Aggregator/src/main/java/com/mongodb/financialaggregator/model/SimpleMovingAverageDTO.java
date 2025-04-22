package com.mongodb.financialaggregator.model;

import java.util.Date;

public record SimpleMovingAverageDTO(double sma, Date date) {}
