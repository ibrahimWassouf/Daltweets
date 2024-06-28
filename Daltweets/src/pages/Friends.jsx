import { useEffect,useState} from "react";
import Friend from "../components/Friend";
import FriendRequest from "../components/FollowRequest";
import axios from "axios";
import "../styles/Friends.css"

function FollowRequests()
{
  const user = JSON.parse(localStorage.getItem("user"));
  const username = user.username
  const [requests,setRequests] = useState([])
  const [followings,setFollowings] = useState([])
  const [followers,setFollowers] = useState([])
  const [accepted,setAccepted] = useState(false)
  const [unfollowed,setUnfollowed] = useState(false)

  const onAccept =  async (username,followerName) => {
    const response = await axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/accept`,
      {
        username,
        followerName,
      }
    )
    
    setAccepted(true)
  }

  const onRemove =  async (username,followerName) => {
    const response = await axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/delete`,
      {
        user : username,
        follower: followerName,
      }
    )
    
    setUnfollowed(true)
  }

  const fetchRequests = async () => {
    await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${username}/requests`)
    .then((response) => {
      setRequests(response.data);
      setAccepted(false)
      setUnfollowed(false)
    })
    .catch((error) => {
      console.log(error);
    });
  }

  const fetchFollowing = async () => {
    await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${username}/following`)
    .then((response) => {
      setFollowings(response.data);
      setAccepted(false)
      setUnfollowed(false)
    })
    .catch((error) => {
      console.log(error);
    });
  }

  const fetchFollowers = async () => {
    await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${username}/followers`)
    .then((response) => {
      setFollowers(response.data)
      setAccepted(false)
      setUnfollowed(false)
    })
    .catch((error) => {
      console.log(error);
    });
  }
  
  useEffect(()=> {
    fetchRequests();
    fetchFollowing();
    fetchFollowers();
  },[accepted,unfollowed])

  return (
    <div className="flex flex-col w-full min-h-screen over-flow-y">
    
    <section className="mr-2">
      <div className="flex flex-col items-center">
      <h1 className="text-4xl font-bold text-center"> Follow Requests</h1>
        <div className="flex flex-col h-96 overflow-y-scroll w-full items-center friend"> 
          { requests.length > 0 ? (requests.map((request,i) => {
              return (
                  <FriendRequest onAccept = {onAccept} onRemove={onRemove} key={i} username={username} followerName={request.follower.username}  />
              )
          })) : 
          <p className="m-10">No follow Requests....</p>}
        </div>
      </div>
    </section>

    <section className="flex flex-row"> 
      <div className="flex flex-col items-center w-full">
        <h1 className="text-4xl font-bold text-center"> Followers</h1>
        <div className="flex flex-col h-64 overflow-y-scroll w-full items-center friend"> 
          { followers.length > 0 ? (followers.map((follower,i) => {
              return (
                  <Friend onRemove = {onRemove} key={i} username={username} followerName={follower.username} />
              )
          })) : <p className="m-10">No Followers....</p>}
        </div>
      </div>


      <div className="flex flex-col items-center w-full">
      <h1 className="text-4xl font-bold text-center"> Following</h1>
        <div className="flex flex-col h-64 overflow-y-scroll w-full items-center friend"> 
        { followings.length > 0 ? (followings.map((following,i) => {
            return (
                <Friend onRemove = {onRemove} key={i} username={following.username} followerName={username} />
            )
        })) : <p className="m-10">No Following....</p>}
        </div>
      </div>
    </section>
    </div>
  )
};

export default FollowRequests;
