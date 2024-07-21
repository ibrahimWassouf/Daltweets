import axios from "axios";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function FriendRequest({ username, followerName, onAccept, onRemove }) {
  const user = JSON.parse(localStorage.getItem("user"));
  const [accepted, setAccepted] = useState(false);
  const isFollower = user.username === username;

  const handleAccept = () => {
    onAccept(username, followerName);
  };

  const handleRemove = () => {
    onRemove(username, followerName);
  };

  return (
    <div className=" mt-8 mb-4 shadow-md rounded-lg flex items-center justify-between p-4">
      <div className="flex-1 mr-24">
        <Link to={`/profile/${encodeURIComponent(followerName)}`} state={{ isFollower }}>
          <div className="font-bold text-black text-xl hover:text-blue-700">
            {followerName}
          </div>
        </Link>
      </div>
      <div className="mr-4">
        <button
          onClick={handleAccept}
          className="bg-gold hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded">
          Accept
        </button>
      </div>

      <div>
        <button
          onClick={handleRemove}
          className="bg-gold hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded">
          Remove
        </button>
      </div>
    </div>
  );
}

export default FriendRequest;
