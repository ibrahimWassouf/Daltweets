/**
 * src/App.jsx
 * Application entry-point
 */

import "./App.css";

// Imports for router
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import SignUp from "./pages/SignUp";
import Login from "./pages/Login";
import Pages from "./pages/Pages";
import ForgotPassword from "./pages/ForgotPassowrd";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/signup" element={<SignUp />} />
        <Route path="*" element={<Pages />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
      </Routes>
    </Router>
  );
}

export default App;
