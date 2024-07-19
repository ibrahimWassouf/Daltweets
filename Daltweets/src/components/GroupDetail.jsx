import React, {useState,useEffect} from 'react'
import { useParams } from 'react-router-dom'
import Post from './Post';
import axios from 'axios';

const GroupDetail = () => {
  const { groupname } = useParams();
  const [group,setGroup] = useState(null);
  const [posts,setPosts] = useState([]);
  useEffect(() => {
    const fetchGroup = async () => {
      const name = JSON.parse(localStorage.getItem('user')).username;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/${groupname}/groupPosts`);
        setPosts(response.data);
        console.log(response.data);
      } catch (error ) {
        console.error('Error fetching groups',error);
      }
    };
    fetchGroup();
    
  },[])
  return (
    <div className="w-screen h-screen">
        GroupDetail
        <div className="w-3/4">
          {posts
            ? posts.map(
              (post, index) => (
                console.log(post),
                (
                  <Post
                    key={index}
                    username={post.user.username}
                    dateCreated={post.dateCreated}
                    {...post}
                  />
                )
              ),
            )
            : (console.log(posts), (<p> Loading posts .... </p>))}
        </div>
    </div>
  )
}

export default GroupDetail