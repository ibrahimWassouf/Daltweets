/**
 * src/App.jsx
 * Application entry-point
 */

import React from "react";
import "./App.css";

// Imports for router
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from "react-router-dom";

import  SignUp  from "./pages/SignUp";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/signup" element={<SignUp />} />
        <Route path="*" element={<Navigate to="/signup" />} />
      </Routes>
    </Router>
  );
}

export default App;
