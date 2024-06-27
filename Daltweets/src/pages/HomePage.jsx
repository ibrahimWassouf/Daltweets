import React, { useState, useEffect} from 'react'
import axios from 'axios';
import Post from '../components/Post';

const HomePage = () => {
  const [posts,setPosts] = useState([]);
  const name = 'user3';
  useEffect( () => {
    const fetchData = async() => {
      try {
        const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/${name}/all`)
        console.log(response.data);
        setPosts(response.data);
      } catch (error) {
        console.error("Error get data", error);
      }
      
    };
    fetchData();  
  },[])

  return (
    <div className="w-screen h-screen">
        HomePage
        
          {posts ? ( posts.map((post,index) => (
            console.log(posts),
            <Post key = {index} username={post.user.username} dateCreated={post.dateCreated}{...post}/>
          )) ) : (
            console.log(posts),
            <p> Loading posts .... </p>
          )}
    </div>
  )
}

export default HomePage