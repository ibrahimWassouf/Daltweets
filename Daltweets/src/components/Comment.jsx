import ReactTimeAgo from "react-time-ago";
import en from "javascript-time-ago/locale/en";
import TimeAgo from "javascript-time-ago";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";

TimeAgo.addDefaultLocale(en);
function Comment({username,text,dateCreated}){
    const user = JSON.parse(localStorage.getItem("user"))
    const [followers, setFollowers] = useState([]);
    const [followings, setFollowings] = useState([]);
  
    const fetchFollowing = async () => {
      await axios
        .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${user.username}/following`)
        .then((response) => {
          setFollowings(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    };
  
    const fetchFollowers = async () => {
      await axios
        .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${user.username}/followers`)
        .then((response) => {
          setFollowers(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    };
  
    const profileDetails = (friendName) => {
      const isFollowing =
        followings.find((user) => user.username === friendName) !== undefined;
      const isFollower =
        followers.find((user) => user.username === friendName) !== undefined;
        console.log(followings)
        
      return {isFriend: isFollower || isFollowing };
    };

    useEffect(()=>{
        fetchFollowers()
        fetchFollowing()
    },[])

  return (
    <div className="border-b border-gray-200 pb-4 mb-4">
            <div className="flex justify-between  mb-2">
              <div>
                <Link to={`/profile/${username}`} state={profileDetails(username)}>
                    <h3 className="font-bold hover:text-yellow-700">{username}</h3>
                </Link>
              </div>
              <span> <ReactTimeAgo date={Date.parse(dateCreated)}></ReactTimeAgo> </span>
            </div>
            <p className="text-gray-700">
              {text}
            </p>
        </div>
  )
};

export default Comment;
