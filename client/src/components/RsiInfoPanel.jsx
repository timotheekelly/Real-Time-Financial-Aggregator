const RsiInfoPanel = ({ latestRsi, company }) => {
    return (
        <div>
            <h2 className="text-lg font-semibold mb-2">Latest RSI for {company}</h2>

            {latestRsi !== null ? (
                <p>
                    Relative Strength Index: <strong>{latestRsi.toFixed(2)}</strong>
                </p>
            ) : (
                <p>Loading RSI...</p>
            )}

            <p className="mt-4 text-gray-600 text-sm">
                <strong>What is RSI?</strong> The Relative Strength Index (RSI) is a momentum indicator that measures the magnitude of recent price changes to
                evaluate overbought or oversold conditions in the price of a stock.
            </p>
            <p className="text-gray-600 text-sm">
                <strong>How is it interpreted?</strong> RSI values range from 0 to 100. A value above 70 indicates that a stock may be overbought,
                while a value below 30 suggests it may be oversold. Traders use these signals to anticipate potential reversals.
            </p>
        </div>
    );
};

export default RsiInfoPanel;
