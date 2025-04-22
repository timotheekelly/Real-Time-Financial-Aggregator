import companySelector from "../components/CompanySelector.jsx";

const API_BASE_URL = "http://localhost:8080/stocks"; // Update if needed

// Fetch all companies
export const getAllCompanies = async () => {
    const response = await fetch(`${API_BASE_URL}/companies`);
    return response.json();
};

// Fetch stock data between dates
export const getStockDataBetweenDates = async (company, startDate, endDate) => {
    const response = await fetch(
        `${API_BASE_URL}/companyStocksBetween?company=${company}&startDate=${startDate}&endDate=${endDate}`
    );
    return response.json();
};

// Fetch Simple Moving Average (SMA)
export const getSmaForCompany = async (company, startDate, endDate) => {
    const response = await fetch(
        `${API_BASE_URL}/sma?company=${company}&startDate=${startDate}&endDate=${endDate}`
    );
    return response.json();
};

export const getEmaForCompany = async (company, startDate, endDate) => {
    const response = await fetch(
        `${API_BASE_URL}/ema?company=${company}&startDate=${startDate}&endDate=${endDate}`
    )
    return response.json();
}

// get live stock data
export const getLiveStockData = async () => {
    const response = await fetch(
        `${API_BASE_URL}/live`
    )
    return response.json();
}

// Stream stock data (handles response manually)
export const streamStockData = async (company, onData) => {
    const response = await fetch(`${API_BASE_URL}/companyStocksStream?company=${company}`);
    const reader = response.body.getReader();
    const decoder = new TextDecoder("utf-8");

    let buffer = "";

    while (true) {
        const { done, value } = await reader.read();
        if (done) break;

        buffer += decoder.decode(value, { stream: true });

        try {
            const json = JSON.parse(buffer);
            onData(json);
            buffer = "";
        } catch (e) {
            // Keep reading until a full JSON object is received
        }
    }
};
