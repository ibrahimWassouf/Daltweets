import { useEffect } from "react";
import axios from "axios";

function Profile() {

    const user = JSON.parse(localStorage.getItem("user"));
    useEffect(() => {
        getProfile();
    }, []);

    const getProfile = async() => {
        try{
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/${user.username}/profile`);
            console.log(response.data);
        }catch (error){
            console.error("Error fetching profile.", error);
        }
    };

    return (
        <div className="container rounded bg-gray-100 mt-3 mb-3 mr-3 ml-3">
            <div className="grid">
                <div className="grid-cols-3">
                    <span className="grid">{user.username}</span>
                    <span className="grid">{user.email}</span>
                    <span className="grid">Online</span>
                </div>
                <div className="grid-cols-1 border-right">
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