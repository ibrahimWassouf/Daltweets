import React, { useEffect, useState } from "react";
import ReactTimeAgo from "react-time-ago";
import TimeAgo from "javascript-time-ago";

import en from "javascript-time-ago/locale/en";
import { FaRegComment, FaRegHeart } from "react-icons/fa";
import { Link } from "react-router-dom";
import axios from "axios";
import Topic from "./Topic";

TimeAgo.addDefaultLocale(en);

function Post({ text, username, commentCount,dateCreated,postId, ...props }) {
  const [followers, setFollowers] = useState([]);
  const [followings, setFollowings] = useState([]);
  const [topics,setTopics] = useState([]);
  console.log(postId);
  let user = JSON.parse(localStorage.getItem("user"));
  const fetchFollowing = async () => {
    await axios
      .get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${user.username}/following`
      )
      .then((response) => {
        setFollowings(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const fetchFollowers = async () => {
    await axios
      .get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${user.username}/followers`
      )
      .then((response) => {
        setFollowers(response.data);
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const fetchTopics = async () => {
    await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/getTopic/${postId}`)
    .then((response) => {
        setTopics(response.data);
        console.log(response.data);
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
    console.log(followers);
    return { isFriend: isFollower || isFollowing };
  };

  useEffect(() => {
    fetchFollowers();
    fetchFollowing();
    fetchTopics();
  }, []);

  return (
    <div>
      <div className="border mb-1 border-black w-full">
        <div className="pl-2">
          <div>
            <Link
              to={`/profile/${encodeURIComponent(username)}`}
              state={profileDetails(username)}
              className="text-black-500 hover:text-yellow-700">
              <span className="font-bold">{username}</span>
            </Link>
            <span className="pl-1 text-gray-500">
              {" "}
              <ReactTimeAgo date={Date.parse(dateCreated)}></ReactTimeAgo>{" "}
            </span>
          </div>
          
          <Link
            to={`/post/${encodeURIComponent(props.id)}`}
            state={{username, text, likeCount: 0, commentCount, dateCreated}}>
            <div>{text}</div>
          </Link>
          <div className="flex ">
            {
              topics.map(
                (topic, index) => (
                  console.log(topic),
                  (
                    <Topic
                      key = {index}
                      topicname={topic.name}
                    />
                  )
                ),
              )
            }
          </div>
          <Link
            to={`/post/${encodeURIComponent(props.id)}`}
            state={{username, text, likeCount: 0, commentCount, dateCreated}}>
            <div className="flex justify-around text-gray-500">
              <button className="flex">
                <FaRegHeart />
                <span>0</span>
              </button>
              <button className="flex ">
                <FaRegComment className="h-5"/>
                <span className="ml-1">{commentCount} </span>
              </button>
            </div>
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Post;
