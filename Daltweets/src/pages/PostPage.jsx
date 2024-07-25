import React, { useEffect, useState } from 'react';
import ReactTimeAgo from 'react-time-ago';
import Comment from '../components/Comment';
import axios from 'axios';
import { Link, useLocation, useParams } from 'react-router-dom';
import Topic from '../components/Topic';

const PostPage = () => {
    const location = useLocation()
    const {username, text, likeCount, commentCount, dateCreated} = location.state || {}
    let user = JSON.parse(localStorage.getItem("user"))
    const [postComments, setPostComments] = useState([])
    const [newComment, setNewComment] = useState('')
    const [followers, setFollowers] = useState([]);
    const [followings, setFollowings] = useState([]);
    const {postId} = useParams();
    const [topics,setTopics] = useState([]);
    const [postTopics,setPostTopics] = useState([]);
  
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
    const fetchTopics = async () => {
        await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/getAllTopics`)
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
        
      return {isFriend: isFollower || isFollowing };
    };

    const fetchComments = async () => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/comment/get/${postId}`);
            setPostComments(response.data);
        } catch (error) {
            console.error("Error get data", error);
        }
    }

    const fetchTopicsbyPost = async () => {
        await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/getTopic/${postId}`)
        .then((response) => {
            setPostTopics(response.data);
            console.log(response.data);
        })
        .catch((error) => {
            console.log(error);
        });
      };

    useEffect(() => {
        fetchComments();
        fetchFollowers();
        fetchFollowing();
        fetchTopics();
        fetchTopicsbyPost();
    }, [])

    const handleCommentSubmit = async (e) => {
        e.preventDefault();
        if (!newComment.trim()) return;

        try {
            await axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/comment/create`, {
                postId,
                comment: newComment,
                username: user.username
            });
            setNewComment('');
            await fetchComments();
        } catch (error) {
            console.error("Error submitting comment", error);
        }
    }

    return (
        <div className="bg-white min-h-screen text-black">
            <header className="sticky top-0 bg-white bg-opacity-90 backdrop-blur-sm border-b border-gray-200 z-10">
                <div className="max-w-3xl mx-auto px-4 py-3 flex items-center justify-center text-xl">
                    Posts
                </div>
            </header>
            <div className="flex">
                <div className="w-5/6">
                    <div className="max-w-3xl mx-auto mt-4 px-4 ">
                        <div className="border-b border-gray-200 pb-4 mb-4">
                            <div className="flex items-center mb-2">
                                <div className="font-bold text-black hover:text-yellow-700">
                                    <Link to={`/profile/${username}`} state={profileDetails(username)}>
                                        <h3 className="font-bold">{username}</h3>
                                    </Link>
                                </div>
                            </div>
                            <p className="text-gray-700 mb-2">{text}</p>
                            <div className="flex ">
                            {
                                postTopics.map(
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
                            <div className="flex justify-between text-gray-500">
                                <span>{likeCount} Likes</span>
                                <span>{commentCount} Comments</span>
                                <span><ReactTimeAgo date={Date.parse(dateCreated)} /></span>
                            </div>
                        </div>

                        
                        <div className="mb-4">
                            <form onSubmit={handleCommentSubmit} className="flex items-center">
                                <div className="flex-grow">
                                    <input
                                        type="text"
                                        placeholder="Write a comment..."
                                        value={newComment}
                                        onChange={(e) => setNewComment(e.target.value)}
                                        className="w-full bg-gray-100 border border-gray-300 rounded-full py-2 px-4 focus:outline-none focus:ring-2 focus:ring-yellow-600"
                                    />
                                </div>
                                <button 
                                    type="submit" 
                                    className="ml-2 bg-yellow-700 text-white rounded-xl px-4 py-2 hover:bg-yellow-500"
                                >
                                    Post
                                </button>
                            </form>
                        </div>

                        
                        <div>
                            <div className='border-b border-t border-gray-200 pb-4 mb-4'>
                                <h2 className="text-xl font-bold mt-4 ">Comments</h2>
                            </div>
                            { postComments?.length > 0 ? (postComments?.map((postComment, i) => (
                                <Comment 
                                    key={i} 
                                    text={postComment.comment} 
                                    username={postComment.user.username} 
                                    dateCreated={postComment.dateCreated} 
                                />
                            ))) :
                            <div className='flex justify-center'> <p> No comments...</p> </div>}
                        </div>
                    </div>
                </div>    
                <div className="pt-5">
                    <div className="px-10 py-5 rounded-xl border-black border h-full">
                        <div className="justify-center flex mb-5">
                            <h1 className="font-bold text-xl border-b border-gray-300 pb-4"> Topics </h1>
                        </div>
                        <div className="">
                        {
                            topics && topics.length > 0 ? 
                        topics?.map(
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
                        : 
                        <p > No Topics....</p>
                        }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PostPage;
