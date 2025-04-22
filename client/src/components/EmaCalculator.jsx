import { useEffect, useState } from "react";
import { getEmaForCompany } from "../api/stockService";

const EmaCalculator = ({ company, startDate, endDate }) => {
    const [latestEma, setLatestEma] = useState(null);
    const [latestDate, setLatestDate] = useState(null);

    useEffect(() => {
        if (company) {
            fetchEma();
        }
    }, [company, startDate, endDate]);

    const fetchEma = async () => {
        const data = await getEmaForCompany(company, startDate, endDate);

        if (data.length > 0) {
            const latestEntry = data[data.length - 1]; // Get the most recent EMA entry
            setLatestEma(latestEntry.ema);
            setLatestDate(new Date(latestEntry.date).toISOString().split("T")[0]); // Format YYYY-MM-DD
        }
    };

    return (
        <div>
            <h2 className="text-lg font-semibold mb-2">Latest EMA for {company}</h2>
            {latestEma !== null ? (
                <p>
                    Exponential Moving Average: <strong>{latestEma.toFixed(2)}</strong> (as of <strong>{latestDate}</strong>)
                </p>
            ) : (
                <p>Loading EMA...</p>
            )}
            <p className="mt-4 text-gray-600 text-sm">
                <strong>What is EMA?</strong> The Exponential Moving Average (EMA) is a moving average that prioritizes recent prices,
                making it more responsive to changes than the SMA.
            </p>
            <p className="text-gray-600 text-sm">
                <strong>How is it calculated?</strong> Unlike SMA, EMA applies an exponential weight to recent prices using a smoothing
                factor (α). A shorter EMA (e.g., 10 days) reacts quickly, while a longer EMA (e.g., 50 days) smooths price movements.
            </p>
        </div>
    );
};

export default EmaCalculator;
