package com.mongodb.financialaggregator.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.util.Date;

@Document(collection = "Live_Data")
@TimeSeries(timeField = "date")
public class LiveStockData {

    @Id
    private ObjectId id;
    private Date date;
    private String company;
    private Double closeLast;
    private Double open;
    private Double low;
    private Double volume;
    private Double high;
    private Double rsi;

    public LiveStockData() {}

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public Double getCloseLast() { return closeLast; }
    public void setCloseLast(Double closeLast) { this.closeLast = closeLast; }

    public Double getOpen() { return open; }
    public void setOpen(Double open) { this.open = open; }

    public Double getLow() { return low; }
    public void setLow(Double low) { this.low = low; }

    public Double getVolume() { return volume; }
    public void setVolume(Double volume) { this.volume = volume; }

    public Double getHigh() { return high; }
    public void setHigh(Double high) { this.high = high; }

    public Double getRsi() { return rsi; }
    public void setRsi(Double rsi) { this.rsi = rsi; }
}
