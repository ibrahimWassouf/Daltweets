import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router"
const Pending = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));
  async function fetchUser() {
    const response = await axios.get(
      `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/${user.username}/profile`,
    );

    if (response && response.data !== undefined) {
      localStorage.setItem("user", JSON.stringify(response.data));
      setUser(JSON.parse(localStorage.getItem("user")));
    }
  }

  useEffect(() => {
    if (user) {
        setTimeout(() => {fetchUser()}, 10000)
    } else {
      navigate("/error")
    }
  });

  return (
    <>
    { user ? 
    <div className="bg-white min-h-screen flex items-center justify-center">
      <div className="max-w-md w-full px-6 py-8 bg-gray-300 rounded-lg shadow-md">
        <h1 className="text-3xl font-bold text-center text-yellow-600 mb-6">
          Sign Up {user && user.status !== "PENDING" ? "Approved" : "Pending"}
        </h1>
        <div className="text-center mb-6">
          <svg
            className="w-16 h-16 text-yellow-600 mx-auto mb-4"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
        </div>
        <p className="text-gray-700 text-center mb-6">
          Thank you for signing up!<br />
           Your request{` to sign up as ${user.username} `}
            {user && user.status !== "PENDING"
            ? "has been approved. You can now login and access Daltweets"
            : "is currently pending approval"}
          .
        </p>
        <div className="flex justify-center">
          <button
            className="bg-yellow-600 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            onClick={() => {
              navigate("/login");
            }}
          >
            Return to Login
          </button>
        </div>
      </div>
    </div>
    : 
            <></>
    }

    </>
  );
};


export default Pending;