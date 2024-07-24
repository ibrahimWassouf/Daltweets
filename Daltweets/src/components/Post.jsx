import React, { useEffect, useState } from "react";
import ReactTimeAgo from "react-time-ago";
import TimeAgo from "javascript-time-ago";

import en from "javascript-time-ago/locale/en";
import { FaRegComment, FaRegHeart } from "react-icons/fa";
import { RiHeartFill } from "react-icons/ri";
import { Link } from "react-router-dom";
import axios from "axios";
import Topic from "./Topic";

TimeAgo.addDefaultLocale(en);

function Post({ text, username, commentCount,dateCreated,postId, ...props }) {
  const [followers, setFollowers] = useState([]);
  const [followings, setFollowings] = useState([]);
  const [topics,setTopics] = useState([]);
  console.log(postId);
  const [liked, setLiked] = useState(props.likedByUser);
  const [likeCount, setLikeCount] = useState(props.likeCount);

  let user = JSON.parse(localStorage.getItem("user"));
  const fetchFollowing = async () => {
    await axios
      .get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${
          user.username
        }/following`
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
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${
          user.username
        }/followers`
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

  const handleLike = async (postId) => {
    if (!liked) {
      await axios
        .post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/add-like`, {
          username: user.username,
          postId: postId,
        })
        .then((res) => {
          console.log(res);
          setLiked(true);
          setLikeCount(likeCount + 1);
        })
        .catch((error) => {
          console.error(error);
        });
    } else {
      await axios
        .post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/unlike`, {
          username: user.username,
          postId: postId,
        })
        .then((res) => {
          console.log(res);
          setLiked(false);
          setLikeCount(likeCount - 1);
        })
        .catch((error) => {
          console.error(error);
        });
    }
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
      <div className="border border-t border-l border-r rounded-lg mb-2 border-gray-400 w-full ">
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
            state={{username, text, likeCount: likeCount, commentCount, dateCreated}}>
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
          <div className="flex justify-around text-gray-500">
            <button className="flex" onClick={() => handleLike(props.id)}>
              {liked ? <RiHeartFill fill="red" /> : <FaRegHeart />}
              <span>{likeCount}</span>
            </button>
            <button className="flex ">
              <FaRegComment className="h-5" />
              <span className="ml-1">{commentCount} </span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Post;
