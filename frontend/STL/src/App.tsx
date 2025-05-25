// src/App.tsx
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import TeamsPage from "./pages/TeamsPage";
import './App.css'
import LeaguesPage from "./pages/LeaguesPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/teams" element={<TeamsPage />} />
        <Route path="/leagues" element={<LeaguesPage />} />
      </Routes>
    </Router>
  );
}

export default App;

