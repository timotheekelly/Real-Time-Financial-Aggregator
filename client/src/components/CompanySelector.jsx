import { useState, useEffect } from "react";
import { getAllCompanies } from "../api/stockService";

const CompanySelector = ({ onSelect }) => {
    const [companies, setCompanies] = useState([]);

    useEffect(() => {
        getAllCompanies().then(setCompanies);
    }, []);

    return (
        <select onChange={(e) => onSelect(e.target.value)}>
            <option value="">Select a company</option>
            {companies.map((company) => (
                <option key={company} value={company}>{company}</option>
            ))}
        </select>
    );
};

export default CompanySelector;
