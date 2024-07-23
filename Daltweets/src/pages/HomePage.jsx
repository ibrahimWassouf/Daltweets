import { useState, useEffect } from "react";
import axios from "axios";
import Post from "../components/Post";
import RecommendedUsers from "../components/RecommendedUsers";

const HomePage = () => {
  const [posts,setPosts] = useState([]);
  useEffect( () => {
    const fetchData = async() => {
      const name = JSON.parse(localStorage.getItem('user')).username;
      try {
        console.log(name);
        const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/${name}/all`);
        console.log(response.data);
        setPosts(response.data);
      } catch (error) {
        console.error("Error get data", error);
      }
    };
    fetchData();
  }, []);

  return (
    <div className="h-screen">
      HomePage
      <div className="flex flex-row flex-wrap ">
        <div className="w-3/4">
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
                    {...post}
                  />
                )
              ),
            )
            : (console.log(posts), (<p> Loading posts .... </p>))}
        </div>
        <div className="m-auto">
          <RecommendedUsers />
        </div>
      </div>
    </div>
  );
};

export default HomePage;
