import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import Post from '../components/Post';
import Topic from '../components/Topic';

const TopicPosts = () => {
    const {topicname} = useParams();
    const [posts,setPosts] = useState([]);
    const [topics,setTopics] = useState([]);
    const fetchPosts = async () => {
        await axios
        .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/getPostsByTopic/${topicname}`)
        .then((response) => {
            setPosts(response.data);
            console.log(response.data);
        })
        .catch((error) => {
            console.log(error);
        })
    }
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
    
    useEffect(() => {
        fetchPosts();
        fetchTopics();
      }, []);
    return (
        <div className="w-full pl-20 pr-16 pt-10">
            <div className="flex flex-row flex-wrap">
                <div className="w-4/5">
                {posts
                    ? posts.map(
                    (post, index) => (
                        console.log(post),
                        (
                        <Post
                            key={index}
                            username={post.creator}
                            dateCreated={post.dateCreated}
                            commentCount = {post.commentCount}
                            postId = {post.id}
                            {...post}
                        />
                        )
                    ),
                    )
                    : (console.log(posts), (<p> Loading posts .... </p>))}
                </div>
                <div className="absolute right-20">
                    <div className="">
                        <div className="px-10 py-5 rounded-xl border-black border h-full min-h-96">
                            <div className="justify-center flex mb-5">
                                <h1 className="font-bold text-xl border-b border-gray-300 pb-4"> Topics </h1>
                            </div>
                            <div className="">
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
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default TopicPosts