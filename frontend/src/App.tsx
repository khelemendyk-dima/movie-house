import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MovieForm from "./components/MovieForm"; // путь к вашему компоненту

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<h1>Welcome to the Movie App</h1>} />
                <Route path="/movies/new" element={<MovieForm />} />
            </Routes>
        </Router>
    );
}

export default App;
