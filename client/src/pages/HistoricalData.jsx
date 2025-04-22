import CompanySelector from "../components/CompanySelector.jsx";
import HistoricalStockChart from "../components/HistoricalStockChart.jsx";
import SmaCalculator from "../components/SmaCalculator.jsx";
import EmaCalculator from "../components/EmaCalculator.jsx";
import { useState } from "react";

const HistoricalData = () => {
    const [selectedCompany, setSelectedCompany] = useState("");
    const [startDate, setStartDate] = useState("2022-07-17");
    const [endDate, setEndDate] = useState("2023-07-17");

    const minDate = "2013-07-17";
    const maxDate = "2023-07-16";

    return (
        <div className="h-screen bg-gray-100 p-6 flex flex-col">
            {/* Header Section */}
            <h1 className="text-3xl font-bold text-center mb-6">Financial Aggregator</h1>

            {/* Controls: Company Selector + Date Inputs */}
            <div className="flex justify-center gap-4 mb-6">
                <CompanySelector onSelect={setSelectedCompany} />

                {/* Start Date Input */}
                <input
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    min={minDate}
                    max={endDate}  // Ensure start date cannot be after the selected end date
                    className="border p-2 rounded shadow-sm"
                />

                {/* End Date Input */}
                <input
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    min={startDate}  // Ensure end date cannot be before the selected start date
                    max={maxDate}
                    className="border p-2 rounded shadow-sm"
                />
            </div>

            {/* Main Layout: Stock Chart on Left (75%) & Metrics on Right (25%) */}
            {selectedCompany && (
                <div className="grid grid-cols-4 gap-6 flex-grow h-full">
                    {/* Stock Chart (75% width) */}
                    <div className="col-span-3">
                        <div className="w-full h-full">
                            <HistoricalStockChart company={selectedCompany} startDate={startDate} endDate={endDate} />
                        </div>
                    </div>

                    {/* Metrics & Calculators (25% width) */}
                    <div className="col-span-1 flex flex-col gap-4">
                        {/* SMA Calculator */}
                        <div className="bg-white p-4 shadow-md rounded-lg">
                            <SmaCalculator company={selectedCompany} startDate={startDate} endDate={endDate} />
                        </div>
                        <div className="bg-white p-4 shadow-md rounded-lg">
                            <EmaCalculator company={selectedCompany} startDate={startDate} endDate={endDate} />
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default HistoricalData;
