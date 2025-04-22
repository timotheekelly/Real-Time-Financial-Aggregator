import { useEffect, useState } from "react";
import { getSmaForCompany } from "../api/stockService";

const SmaCalculator = ({ company, startDate, endDate }) => {
    const [latestSma, setLatestSma] = useState(null);
    const [latestDate, setLatestDate] = useState(null);

    useEffect(() => {
        if (company) {
            fetchSma();
        }
    }, [company, startDate, endDate]);

    const fetchSma = async () => {
        const data = await getSmaForCompany(company, startDate, endDate);

        if (data.length > 0) {
            const latestEntry = data[data.length - 1]; // Get the last SMA entry
            setLatestSma(latestEntry.sma);
            setLatestDate(new Date(latestEntry.date).toISOString().split("T")[0]); // Format YYYY-MM-DD
        }
    };

    return (
        <div>
            <h2 className="text-lg font-semibold mb-2">Latest SMA for {company}</h2>
            {latestSma !== null ? (
                <p>
                    Simple Moving Average: <strong>{latestSma.toFixed(2)}</strong> (as of <strong>{latestDate}</strong>)
                </p>
            ) : (
                <p>Loading SMA...</p>
            )}
            <p className="mt-4 text-gray-600 text-sm">
                <strong>What is SMA?</strong> The Simple Moving Average (SMA) smooths price data by averaging closing prices over a set period,
                helping identify trends by reducing short-term fluctuations.
            </p>
            <p className="text-gray-600 text-sm">
                <strong>How is it calculated?</strong> The SMA adds closing prices over a chosen period (e.g., 10, 20, or 50 days) and divides by the number of periods.
                A rising SMA signals an uptrend, while a falling SMA suggests a downtrend.
            </p>
        </div>
    );
};

export default SmaCalculator;
