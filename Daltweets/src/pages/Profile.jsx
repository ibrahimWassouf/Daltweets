import { useState, useEffect } from "react";
import axios from "axios";

function Profile() {
    const [user, setUser] = useState({});

    useEffect(() => {
        getProfile();
    }, []);

    const getProfile = async() => {
        try{
            const response = axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/`);
            console.log(response.data.user);
            //setUser(response.data);
            setUser(JSON.parse(localStorage.getItem("user")));
        }catch (error){
            console.error("Error fetching profile.", error);
        }
    };

    return (
        <div className="container rounded bg-white mt-5 mb-5">
            <div className="row">
                <div className="col-md-3 border-right">
                    <!--pfp?-->
                    <span className="font-weight-bold">{user}</span>
                    <span className="text-black-50">{user.email}</span>
                    <span className="text-black-50">{user.status}</span>
                </div>
                <div className="col-md-5 border-right">
                    <div className="p-3 py-5">
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h1 className="text-center">Profile</h1>
                        </div>
                        <div className="row mt-2">
                            <div className="col-md-6">
                                <h2>FirstName</h2>
                            </div>
                            <div className="col-md-6">
                                <h2>LastName</h2>
                            </div>
                        </div>
                        <div className="row mt-3">
                            <div className="col-md-12">
                                <h2>About</h2>
                            </div>
                            <div className="col-md-12">
                                <p>Bio...</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Profile;