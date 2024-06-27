import React from "react";
import { Alert } from "react-native";
import { Outlet, Navigate } from 'react-router-dom';

// Note: This may need refactoring depending on how we want to handle authentication
const user = localstorage.getItem("user");

const ProtectedRoutes = () => {

    if(user === null){
        Alert.alert("Error", "You are not logged in. Please log in or sign up.", [
            {
                text: 'OK',
                onPress: () => console.log("Redirecting to log in page."),
                style: 'default'
            }
        ])
        return <Navigate to="/login" replace={true} />;
    }

    return <Outlet/>;
};
export default ProtectedRoutes;