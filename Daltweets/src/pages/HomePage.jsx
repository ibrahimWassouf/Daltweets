import React, { useState, useEffect} from 'react'
import NavBar from '../components/NavBar'
import axios from 'axios';
import Post from '../components/Post';

const HomePage = () => {
  const examplePosts = [
    {
      id: 1,
      user: {
        id: 1,
        bio: "User bio text",
        username: "user1",
        email: "user1@example.com",
        dateCreated: "2024-06-26T10:30:00Z",
        accountDeleted: false,
        role: "USER",
        status: "ACTIVE"
      },
      text: "This is the first post!",
      dateCreated: "2024-06-26T10:30:00Z",
      isDeleted: false,
      isEdited: false
    },
    {
      id: 2,
      user: {
        id: 2,
        bio: "Another user bio",
        username: "user2",
        email: "user2@example.com",
        dateCreated: "2024-06-25T15:45:00Z",
        accountDeleted: false,
        role: "USER",
        status: "ACTIVE"
      },
      text: "Another post here.",
      dateCreated: "2024-06-25T15:45:00Z",
      isDeleted: false,
      isEdited: false
    },
    {
      id: 3,
      user: {
        id: 3,
        bio: "User 3 bio",
        username: "user3",
        email: "user3@example.com",
        dateCreated: "2024-06-24T20:00:00Z",
        accountDeleted: false,
        role: "USER",
        status: "ACTIVE"
      },
      text: "Last post of the day.",
      dateCreated: "2024-06-24T20:00:00Z",
      isDeleted: false,
      isEdited: false
    }
  ];
  const [posts,setPost] = useState([]);
  useEffect( () => {
    const fetchData = async() => {
      try {
        const response = axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/{username}/all`)
        console.log(response);
        setPost(response.data);
      } catch (error) {
        console.error("Error get data", error);
      }
    };
    setPost(examplePosts);
  },[])
  return (
    <div>
        HomePage
        <ul>
          {posts ? ( posts.map((post,index) => (
            <Post key = {index} username={post.user.username} {...post}/>
          )) ) : (
            <p> Loading posts .... </p>
          )}
        </ul>
    </div>
  )
}

export default HomePage