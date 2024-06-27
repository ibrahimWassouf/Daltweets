import React from "react";
import { Navigate } from 'react-router-dom';

// Note: This may need refactoring depending on how we want to handle authentication
const user = localStorage.getItem("user");

const ProtectedRoutes = () => {

    if(user === null){
        alert('Error: You are not logged in. Please log in or sign up to access this page.');
        return <Navigate to="/login" replace={true} />;
    }

    return <Pages/>;
};
export default ProtectedRoutes;