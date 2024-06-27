/**
 * src/App.jsx
 * Application entry-point
 */

import "./App.css";

// Imports for router
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";

import SignUp from "./pages/SignUp";
import Login from "./pages/Login";
import HomePage from "./pages/HomePage";
import Pages from "./pages/Pages";
import ProtectedRoutes from "./components/ProtectedRoutes";

function App() {
  return (
    <Router>

      <Routes>
        <Route path="/signup" element={<SignUp />} />
        <Route path="/login" element={<Login />} />
        
        <ProtectedRoutes>
          <Route path="/pages/*" element={<Pages/>} />
        </ProtectedRoutes>
      </Routes>
    </Router>
  );
}

export default App;
