import React from 'react'
import HomePage from './HomePage';
import NavBar from '../components/NavBar';


import {
    BrowserRouter as Router,
    Route,
    Routes,
    Navigate,
  } from "react-router-dom";
const Pages = () => {
  return (
    <div className = "flex ">
            <NavBar/>
            <Routes>
                <Route path="/home" element={<HomePage/>} />
            </Routes>
    </div>
  )
}

export default Pages