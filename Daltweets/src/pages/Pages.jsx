import React, { useEffect, useState } from 'react'
import HomePage from './HomePage';
import NavBar from '../components/NavBar';


import {
    BrowserRouter as Router,
    Route,
    Routes,
  } from "react-router-dom";
import Error from './Error';
const Pages = () => {
  const user = localStorage.getItem("user");
  return (
    <>
    {
      user ? (
    <div className = "flex ">
            <NavBar/>
            <Routes>
                <Route path="/home" element={<HomePage/>} />
            </Routes>
    </div>) : 
    (
        <Error /> 
    )
    }
    </>
  )
}

export default Pages