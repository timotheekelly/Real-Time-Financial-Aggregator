import { useState, useEffect } from "react";
import Chart from "react-apexcharts";
import { getStockDataBetweenDates, getSmaForCompany, getEmaForCompany } from "../api/stockService";

const HistoricalStockChart = ({ company, startDate, endDate }) => {
    const [stockData, setStockData] = useState([]);
    const [smaData, setSmaData] = useState([]);
    const [emaData, setEmaData] = useState([]);

    useEffect(() => {
        if (company) {
            fetchStockData();
            fetchSmaData();
            fetchEmaData();
        }
    }, [company, startDate, endDate]);

    const fetchStockData = async () => {
        const data = await getStockDataBetweenDates(company, startDate, endDate);

        // Format data for candlestick chart
        const formattedData = data.map(stock => ({
            x: new Date(stock.date), // Ensure date format consistency
            y: [stock.open, stock.high, stock.low, stock.closeLast]
        }));

        setStockData(formattedData);
    };

    const fetchSmaData = async () => {
        const data = await getSmaForCompany(company, startDate, endDate);

        // Format data for line chart
        const formattedData = data.map(stock => ({
            x: new Date(stock.date),
            y: stock.sma
        }));

        setSmaData(formattedData);
    };

    const fetchEmaData = async () => {
        const data = await getEmaForCompany(company, startDate, endDate);

        // Format EMA data correctly
        const formattedData = data.map(stock => ({
            x: new Date(stock.date),
            y: stock.ema
        }));

        setEmaData(formattedData);
    };

    const chartOptions = {
        chart: {
            type: "candlestick",
            background: "#fff",
            toolbar: { show: true }
        },
        xaxis: {
            type: "datetime",
            labels: {
                style: { colors: "#555" }
            }
        },
        yaxis: {
            tooltip: { enabled: true },
            labels: {
                formatter: (value) => Math.round(value),
                style: { colors: "#555" }
            }
        },
        tooltip: {
            theme: "light"
        },
        stroke: {
            width: [1, 2, 2], // 1px for candlestick, 2px for SMA, 2px for EMA
            curve: "smooth",
            colors: ["#4F46E5", "#FF9800", "#46D9FF"] // Ensure stroke colors match the legend
        },
        plotOptions: {
            candlestick: {
                colors: {
                    upward: "#26a69a", // Green for bullish candles
                    downward: "#ef5350" // Red for bearish candles
                }
            }
        },
        colors: ["#4F46E5", "#FF9800", "#46D9FF"], // Ensure distinct colors for the series
        legend: {
            position: "top"
        }
    };


    const series = [
        {
            name: "Stock Price",
            type: "candlestick",
            data: stockData
        },
        {
            name: "Simple Moving Average (SMA)",
            type: "line",
            data: smaData
        },
        {
            name: "Exponential Moving Average (EMA)",
            type: "line",
            data: emaData
        }
    ];

    return (
        <div className="w-full h-[500px] p-6 bg-white shadow-lg rounded-lg flex flex-col">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Stock Price for {company}</h2>

            <div className="flex-grow">
                {stockData.length > 0 && smaData.length > 0 && emaData.length > 0 ? (
                    <Chart options={chartOptions} series={series} type="candlestick" height="100%" />
                ) : (
                    <p className="text-center text-gray-500">Loading stock data...</p>
                )}
            </div>
        </div>
    );
};

export default HistoricalStockChart;
