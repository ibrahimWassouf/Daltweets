import axios from "axios";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function Friend({ username, followerName, action, onRemove }) {
  const [accepted, setAccepted] = useState(false);
  const user = JSON.parse(localStorage.getItem("user"));
  const displayedName = user.username === username ? followerName : username;
  const isFollower = user.username === username;

  const handleRemove = () => {
    onRemove(username, followerName);
  };

  return (
    <div className=" mt-12 mb-4 w-96 bg-cover shadow-md rounded-lg flex items-center justify-between p-4">
      <div className="flex-1 mr-20">
        <Link to="/profile" state={{ username: displayedName, isFriend: true }}>
          <div className="font-bold text-black text-xl hover:text-blue-700">
            {displayedName}
          </div>
        </Link>
      </div>
      <div>
        <button
          onClick={handleRemove}
          className="bg-gold hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded">
          {isFollower ? "Remove" : "Unfollow"}
        </button>
      </div>
    </div>
  );
}

export default Friend;
