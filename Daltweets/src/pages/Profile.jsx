import { useEffect } from "react";
import axios from "axios";
import { GoDotFill } from "react-icons/go";


function Profile() {

    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        getProfile();
    }, []);

    const getProfile = async() => {
        try{
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/${user.username}/profile`);
            console.log(response.data)
        }catch (error){
            console.error("Error fetching profile.", error);
        }
    };

    return (
        <div className="container rounded bg-gray-100 mt-3 mb-3 mr-3 ml-3">
            <div className="grid">
                <div className="grid-cols-1 ml-5 mt-10">
                    <div className="font-bold text-3xl">{user.username}
                        <button className="bg-blue-300 hover:bg-yellow-200 rounded-full ml-1 py-0 px-2 text-base">
                            Edit
                        </button>
                    </div>
                    <div className="text-base">{user.email}</div>
                    <div className="text-base italic flex">{user.status}<GoDotFill color="green" className="flex items-center justify-center"/></div>
                </div>
                <div className="grid-cols-1 border-right">
                    <div className="p-3 py-5">
                        <div className="row mt-3">
                            <div className="text-xl">
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