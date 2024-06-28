import axios from "axios";
import { useEffect, useState } from "react";


function FriendRequest({username,followerName,onAccept,onRemove}){
  const [accepted,setAccepted] = useState(false)
  const isFollower = user.username === username
  
  const handleAccept = () => {
    onAccept(username,followerName)
  }

  const handleRemove = () =>
    {
      onRemove(username,followerName)
    }

  return (
    <div className=" mt-8 mb-4  bg-black shadow-md rounded-lg flex items-center justify-between p-4">
      <div className="flex-1 mr-24">
      <Link 
        to="/profile" 
        state={{ username: followerName, isFollower  }}
        className="text-blue-500 hover:text-blue-700"
      >
        <div className="font-bold text-white text-xl">{ followerName }</div>
      </Link>
      </div>
      <div className="mr-4">
        <button
          onClick={handleAccept}
          className="bg-gold hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded"
        >
          Accept
        </button>
      </div>

      <div>
        <button
          onClick={handleRemove}
          className="bg-gold hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded"
        >
          Remove
        </button>
      </div>
    </div>
  );
};


export default FriendRequest;