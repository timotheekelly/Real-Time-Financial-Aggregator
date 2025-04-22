import { useState, useEffect, useRef } from "react";
import Chart from "react-apexcharts";
import { getLiveStockData } from "../api/stockService";
import RsiInfoPanel from "./RsiInfoPanel";


const MAX_POINTS = 100;

const LiveStockChart = () => {
    const [stockData, setStockData] = useState([]);
    const [rsiData, setRsiData] = useState([]);
    const [company, setCompany] = useState("DOOF");
    const intervalRef = useRef(null);
    const latestRsiValue = rsiData.length > 0 ? rsiData[rsiData.length - 1].y : null;


    useEffect(() => {
        fetchStockData(); // Initial fetch

        // Set up interval for real-time updates
        intervalRef.current = setInterval(fetchStockData, 3000);

        return () => clearInterval(intervalRef.current); // Cleanup
    }, []);

    const fetchStockData = async () => {
        const data = await getLiveStockData();

        const sorted = data
            .sort((a, b) => new Date(a.date) - new Date(b.date))
            .slice(-MAX_POINTS); // Only last 100

        const candle = sorted.map(stock => ({
            x: new Date(stock.date),
            y: [stock.open, stock.high, stock.low, stock.closeLast]
        }));

        const rsi = sorted
            .filter(stock => stock.rsi !== undefined && stock.rsi !== null)
            .map(stock => ({
                x: new Date(stock.date),
                y: Math.round(stock.rsi * 100) / 100
            }));

        setStockData(candle);
        setRsiData(rsi);
        if (sorted.length > 0) setCompany(sorted[sorted.length - 1].company);
    };

    const chartOptions = {
        chart: {
            background: "#fff",
            toolbar: { show: true },
            animations: {
                enabled: true,
                easing: "linear",
                dynamicAnimation: {
                    speed: 500
                }
            }
        },
        xaxis: {
            type: "datetime",
            labels: { style: { colors: "#555" } }
        },
        yaxis: [
            {
                seriesName: "Stock Price",
                tooltip: { enabled: true },
                labels: { formatter: val => Math.round(val), style: { colors: "#555" } },
                title: { text: "Price", style: { color: "#4F46E5" } }
            },
            {
                opposite: true,
                seriesName: "RSI",
                min: 0,
                max: 100,
                labels: { formatter: val => Math.round(val), style: { colors: "#FF9800" } },
                title: { text: "RSI", style: { color: "#FF9800" } }
            }
        ],
        annotations: {
            yaxis: [
                {
                    y: 70,
                    yAxisIndex: 1,
                    borderColor: "#FF5722",
                    label: {
                        borderColor: "#FF5722",
                        style: {
                            color: "#fff",
                            background: "#FF5722"
                        },
                        text: "Overbought (70)"
                    }
                },
                {
                    y: 30,
                    yAxisIndex: 1,
                    borderColor: "#2196F3",
                    label: {
                        borderColor: "#2196F3",
                        style: {
                            color: "#fff",
                            background: "#2196F3"
                        },
                        text: "Oversold (30)"
                    }
                }
            ]
        },
        tooltip: {
            shared: true,
            theme: "light"
        },
        stroke: {
            width: [1, 2],
            curve: "smooth"
        },
        plotOptions: {
            candlestick: {
                colors: {
                    upward: "#26a69a",
                    downward: "#ef5350"
                }
            }
        },
        colors: ["#4F46E5", "#FF9800"],
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
            name: "RSI",
            type: "line",
            data: rsiData,
            yAxisIndex: 1
        }
    ];

    return (
        <div className="w-full h-[600px] p-6 bg-white shadow-lg rounded-lg flex flex-col">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">Live Stock Price for {company}</h2>

            <div className="flex-grow flex flex-row">
                {/* Left: Chart */}
                <div className="w-3/4 pr-4">
                    {stockData.length > 0 ? (
                        <Chart options={chartOptions} series={series} type="line" height="100%"/>
                    ) : (
                        <p className="text-center text-gray-500">Loading stock data...</p>
                    )}
                </div>

                {/* Right: RSI Info Panel */}
                <div className="w-1/4 pl-4 border-l border-gray-200">
                    <RsiInfoPanel latestRsi={latestRsiValue} company={company}/>
                </div>
            </div>
        </div>

    );
};

export default LiveStockChart;
