import { useState, useEffect } from "react";
import axios from "axios";

function Profile() {
    const [user, setUser] = useState(null);
    const [getError, setGetError] = useState(false);

    useEffect(() => {
        getProfile();
    }, []);

    const getProfile = () => {
        axios
            .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/user/profile/{username}`)
            .then(response => {
                console.log(response.data);
                setGetError(false);
                setUser(response.data);
            })
            .catch((getError) => {
                console.log(getError);
                setGetError(true);
                console.log(getError.response.data);
            });
    };

    return (
        <div className="profile-page">
            <div className="username">
                <h1>
                    Username
                </h1>
            </div>
            <div className="full-name">
                <h2>
                    FirstName LastName
                </h2>
            </div>
        </div>
    );
}

export default Profile;