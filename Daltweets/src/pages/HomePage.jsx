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
    <div className="w-screen h-full min-h-screen pl-20 pr-16 pt-7">
      <div className="pb-5  w-3/4 flex justify-center text-xl font-semibold">
        HomePage
      </div>
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
                    postId = {post.id}
                    {...post}
                  />
                )
              ),
            )
            : (console.log(posts), (<p> Loading posts .... </p>))}
        </div>
        <div className="m-auto pl-6 absolute top-7 right-8">
          <RecommendedUsers />
        </div>
      </div>
    </div>
  );
};

export default HomePage;
