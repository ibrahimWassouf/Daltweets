import axios from "axios";
import { useEffect, useState } from "react";


function Friend({username,followerName,action,onRemove}){
  const [accepted,setAccepted] = useState(false)
  const user = {username : "Tobi"}
  //JSON.parse(localStorage.getItem("user"));

  const handleRemove = () => {
    onRemove(username,followerName)
  }

  return (
    <div className=" mt-12 mb-4 bg-black bg-cover shadow-md rounded-lg flex items-center justify-between p-4">
      <div className="flex-1 mr-20">
        <div className="font-bold text-white text-xl">{
           user.username === username ? followerName : username }</div>
      </div>
      <div>
        <button
          onClick={handleRemove}
          className="bg-gold hover:bg-yellow-500 text-black font-bold py-2 px-4 rounded"
        >{user.username === username ? "Remove" : "Unfollow" }</button>
      </div>
    </div>
  );
};


export default Friend;