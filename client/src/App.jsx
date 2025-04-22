import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import HistoricalData from "./pages/HistoricalData.jsx";
import LiveData from "./pages/LiveData.jsx";
import "./App.css";

function App() {
    return (
        <Router>
            <div className="min-h-screen bg-gray-100">
                <nav className="bg-gray-800 p-4">
                    <ul className="flex space-x-6">
                        <li>
                            <Link to="/" className="text-white font-semibold hover:text-gray-300">Live Data</Link>
                        </li>
                        <li>
                            <Link to="/historical" className="text-white font-semibold hover:text-gray-300">Historical Data</Link>
                        </li>
                    </ul>
                </nav>

                <div className="p-6">
                    <Routes>
                        <Route path="/" element={<LiveData />} />
                        <Route path="/historical" element={<HistoricalData />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
