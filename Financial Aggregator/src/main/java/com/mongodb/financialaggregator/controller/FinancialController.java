package com.mongodb.financialaggregator.controller;

import com.mongodb.financialaggregator.model.ExponentialMovingAverageDTO;
import com.mongodb.financialaggregator.model.LiveStockData;
import com.mongodb.financialaggregator.model.SimpleMovingAverageDTO;
import com.mongodb.financialaggregator.model.StockMarketData;
import com.mongodb.financialaggregator.service.FinancialService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // Adjust if needed
@RestController
@RequestMapping(path = "/stocks")
public class FinancialController {
    private final FinancialService financialService;

    public FinancialController(FinancialService financialService) {
        this.financialService = financialService;
    }

    // test
    // GET http://localhost:8080/stocks/companies
    // curl -v "http://localhost:8080/stocks/companies"
    @GetMapping(value = "/companies", produces = "application/json")
    public List<String> getAllCompanies() {
        return financialService.getAllCompanies();
    }

    // test
    // GET http://localhost:8080/stocks/companyStocksBetween?company=AAPL&startDate=2022-17-10&endDate=2023-07-17
    // curl -v "http://localhost:8080/stocks/companyStocksBetween?company=AAPL&startDate=2022-07-17&endDate=2023-07-17"
    @GetMapping(value = "/companyStocksBetween", produces = "application/json")
    public List<StockMarketData> getStockDataBetweenDates(
            @RequestParam String company,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return financialService.getStockDataBetweenDates(company, startDate, endDate);
    };

    // test
    // GET http://localhost:8080/stocks/sma?company=AAPL&startDate=2023-07-10&endDate=2023-07-17
    // curl -v "http://localhost:8080/stocks/sma?company=AAPL&startDate=2022-07-10&endDate=2023-07-17"
    @GetMapping(value = "/sma", produces = "application/json")
    public List<SimpleMovingAverageDTO> getSmaForCompany(
            @RequestParam String company,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return financialService.getSimpleMovingAverage(company, startDate, endDate);
    };

    // test
    // GET http://localhost:8080/stocks/ema?company=AAPL&startDate=2023-07-10&endDate=2023-07-17
    // curl -v "http://localhost:8080/stocks/sma?company=AAPL&startDate=2023-07-10&endDate=2023-07-17"
    @GetMapping(value= "/ema", produces = "application/json")
    public List<ExponentialMovingAverageDTO> getEmaForCompany(
            @RequestParam String company,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return financialService.getExponentialMovingAverage(company, startDate, endDate);
    };

    // test
    // GET http://localhost:8080/stocks/live
    // curl -v "http://localhost:8080/stocks/live"
    @GetMapping(value= "/live", produces = "application/json")
    public List<LiveStockData> getLiveData() {
        return financialService.getLiveData();
    };

}
