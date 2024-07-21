import { useEffect, useState } from "react";
import axios from "axios";
import { GoDotFill } from "react-icons/go";
import { FaGear } from "react-icons/fa6";
import { useLocation, useNavigate, useParams } from "react-router-dom";

function Profile() {
  const location = useLocation();
  const {isFriend } = location.state || {};
  const loggedInUser = JSON.parse(localStorage.getItem("user"));
  const {username} = useParams() || loggedInUser.username;
  const [backendUser, setBackendUser] = useState({});
  const isLoggedInUser = !backendUser || backendUser.username == loggedInUser.username;
  const user = backendUser ? backendUser : loggedInUser;
  let statusColor = "";
  switch (user.status) {
    case "ONLINE":
      statusColor = "green";
      break;
    case "OFFLINE":
      statusColor = "red";
      break;
    case "DEACTIVATED":
      statusColor = "grey";
      break;
    default:
      statusColor = "yellow";
  }

  useEffect(() => {
    getProfile();
  }, [username]);

  const getProfile = async () => {
    
    try {
      const response = await axios.get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/${username}/profile`
      );
      setBackendUser(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching profile.", error);
    }
  };


  let navigae = useNavigate();
  const routeChange = () => {
    let path = "/updateUser";
    navigae(path);
  };
  return (
    <div className="container rounded bg-gray-100 mt-3 mb-3 mr-3 ml-3">
      <div className="grid">
        <div className="grid-cols-1 ml-5 mt-10">
          <div className="font-bold text-3xl">
            {user.username}
            {isLoggedInUser ? (
              <button
                className="bg-blue-300 hover:bg-yellow-200 rounded-full ml-1 py-0 px-2 text-base"
                onClick={routeChange}>
                <FaGear className="mr-1 inline-block mb-1" />
                Edit
              </button>
            ) : (
              <></>
            )}
          </div>
          <div className="text-base">{user.email}</div>
          {isFriend || isLoggedInUser ? (
            <div className="text-base italic flex">
              {user.status}
              <GoDotFill
                color={statusColor}
                className="flex items-center justify-center"
              />
            </div>
          ) : (
            <></>
          )}
        </div>
        <div className="grid-cols-1 border-right">
          <div className="p-3 py-5">
            <div className="row mt-3">
              <div className="text-xl">
                <h2>About</h2>
              </div>
              <div className="col-md-12">
                <p>{user.bio}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
