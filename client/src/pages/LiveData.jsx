import React from "react";
import LiveStockChart from "../components/LiveStockChart.jsx";


const LiveData = () => {


    return (
        <div className="h-screen bg-gray-100 p-6 flex flex-col">
            {/* Header Section */}
            <h1 className="text-3xl font-bold text-center mb-6">Financial Aggregator</h1>
            {(
                <div className="grid grid-cols-4 gap-6 flex-grow h-full">
                    <div className="col-span-4">
                        <div className="w-full h-full">
                            <LiveStockChart />
                        </div>
                    </div>

                </div>
            )}
        </div>
    );
};

export default LiveData;