package com.mongodb.financialaggregator.service;

import com.mongodb.financialaggregator.model.StockMarketData;
import com.mongodb.financialaggregator.repository.FinancialRepository;
import org.bson.types.ObjectId;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
//public class ReadInHistoricalData implements ApplicationRunner {
public class ReadInHistoricalData {

    private final FinancialRepository financialRepository;
    private static final int BATCH_SIZE = 500; // Define batch size for bulk writes

    public ReadInHistoricalData(FinancialRepository financialRepository) {
        this.financialRepository = financialRepository;
    }

//    @Override
//    public void run(org.springframework.boot.ApplicationArguments args) {
//        loadData();
//    }

    public void loadData() {
        String fileName = "data.csv"; // File inside resources directory

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(fileName).getInputStream()))) {
            // Skip header line
            String line = reader.readLine();

            List<StockMarketData> batch = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                StockMarketData data = parseCsvLine(line);
                if (data != null) {
                    batch.add(data);
                }

                // When batch size is reached, process it
                if (batch.size() == BATCH_SIZE) {
                    financialRepository.saveAll(batch);
                    batch.clear(); // Clear the batch for the next set of lines
                }
            }

            // Process any remaining records
            if (!batch.isEmpty()) {
                financialRepository.saveAll(batch);
            }

            System.out.println("Data import completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StockMarketData parseCsvLine(String line) {
        try {
            String[] fields = line.split(","); // Assuming CSV is comma-separated

            if (fields.length < 7) {
                System.err.println("Skipping invalid line: " + line);
                return null;
            }

            String company = fields[0].trim();
            Date date = parseDate(fields[1].trim()); // Parse the date properly
            Double closeLast = parseDouble(fields[2]);
            Double volume = parseDouble(fields[3]);
            Double open = parseDouble(fields[4]);
            Double high = parseDouble(fields[5]);
            Double low = parseDouble(fields[6]);

            if (date == null) {
                System.err.println("Skipping row due to invalid date: " + line);
                return null;
            }

            return new StockMarketData(date, company, closeLast, open, low, volume, high);
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            return null;
        }
    }


    private Date parseDate(String dateStr) {
        String[] dateFormats = {"MM/dd/yyyy", "MM-dd-yyyy"}; // Handle both formats

        for (String format : dateFormats) {
            try {
                return new SimpleDateFormat(format, Locale.US).parse(dateStr);
            } catch (ParseException ignored) {
            }
        }

        System.err.println("Error parsing date: " + dateStr);
        return null;
    }


    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace("$", "").trim());
        } catch (NumberFormatException e) {
            return null; // Handle cases where parsing fails
        }
    }
}
