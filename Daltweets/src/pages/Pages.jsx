import React, { useEffect, useState } from 'react'
import HomePage from './HomePage';
import NavBar from '../components/NavBar';
import CreatePost from './CreatePost';
import Profile from './Profile';

import {
    BrowserRouter as Router,
    Route,
    Routes,
  } from "react-router-dom";
import Friends from './Friends';
import Error from './Error';
import UpdateUser from './UpdateUser';
import Group from './Group';
const Pages = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <>
    {
      user ? (
    <div className = "flex ">
            <NavBar/>
            <Routes>
                <Route path="/home" element={<HomePage/>} />
                <Route path="/friends" element={<Friends />} />
                <Route path="/create" element = {<CreatePost/>} />
                <Route path="/profile" element = {<Profile/>} />
                <Route path="/updateUser" element = {<UpdateUser/>} />
                <Route path="/group" element={<Group/>} />
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