import React, { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import Post from "./Post";
import axios from "axios";
import { IoPersonAddSharp } from "react-icons/io5";
import AddMembers from "../pages/AddMembers";

const GroupDetail = () => {
  const { groupname } = useParams();
  const [group, setGroup] = useState(null);
  const [posts, setPosts] = useState([]);
  const [admins, setAdmins] = useState([]);
  const [members, setMembers] = useState([]);
  const [addMember, setAddMember] = useState(false);
  const [admin, setAdmin] = useState(null);
  const [addAdmin, setAddadmin] = useState(null);
  const [followers, setFollowers] = useState([]);
  const [followings, setFollowings] = useState([]);
  const name = JSON.parse(localStorage.getItem("user")).username;

  const fetchFollowing = async () => {
    await axios
      .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${name}/following`)
      .then((response) => {
        setFollowings(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const fetchFollowers = async () => {
    await axios
      .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${name}/followers`)
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

    return { isFriend: isFollower || isFollowing };
  };

  useEffect(() => {
    const fetchGroup = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/${groupname}/groupPosts`
        );
        setPosts(response.data);

        const response1 = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/${groupname}/admins`
        );
        setAdmins(response1.data);

        const response2 = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/${groupname}/members`
        );
        setMembers(response2.data);

        for (const admin of response1.data) {
          if (admin.username === name) {
            setAdmin(true);
            break;
          }
        }
      } catch (error) {
        console.error("Error fetching groups", error);
      }
    };
    fetchGroup();
    fetchFollowers();
    fetchFollowing();
  }, []);

  return (
    <div className="min-h-screen flex mt-4 ml-2">
      <div className="w-3/4">
        <div className="justify-center flex mb-5">
          <h1 className="font-bold text-xl"> {groupname}</h1>
        </div>
        {posts
          ? posts.map((post, index) => (
              <Post
                key={index}
                username={post.creator}
                dateCreated={post.dateCreated}
                commentCount={post.commentCount}
                {...post}
              />
            ))
          : (console.log(posts), (<p> Loading posts .... </p>))}
      </div>

      <div className="ml-14 border-black border w-1/6 h-svh">
        <div className="justify-center flex">
          <h3 className="font-bold"> Admins </h3>
          {admin == true && (
            <div>
              <button
                onClick={() => {
                  setAddMember(true);
                  setAddadmin(true);
                }}>
                <IoPersonAddSharp className="ml-2" />
              </button>
              <AddMembers
                isVisible={addMember}
                onClose={() => setAddMember(false)}
                groupName={groupname}
                isAdmin={addAdmin}
              />
            </div>
          )}
        </div>
        <div className="ml-4">
          {admins
            ? admins.map((admin, index) => (
                <div key={index}>
                  <Link
                    to={`/profile/${encodeURIComponent(admin.username)}`}
                    state={profileDetails(admin.username)}
                    className="text-black-500 hover:text-yellow-700">
                    {admin.username}
                  </Link>
                </div>
              ))
            : (console.log(admins), (<p> no admins .... </p>))}
        </div>
        <div className="justify-center flex items-center">
          <h3 className="font-bold"> Members </h3>
          <button
            onClick={() => {
              setAddMember(true);
              setAddadmin(false);
            }}>
            <IoPersonAddSharp className="ml-2" />
          </button>
          <AddMembers
            isVisible={addMember}
            onClose={() => setAddMember(false)}
            groupName={groupname}
            isAdmin={addAdmin}
          />
        </div>
        <div className="ml-4">
          {members
            ? members.map((member, index) => (
                <div key={index}>
                  <Link
                    to={`/profile/${encodeURIComponent(member.username)}`}
                    state={profileDetails(member.username)}
                    className="text-black-500 hover:text-yellow-700">
                    {member.username}
                  </Link>
                </div>
              ))
            : (console.log(members), (<p> no members .... </p>))}
        </div>
      </div>
    </div>
  );
};

export default GroupDetail;
