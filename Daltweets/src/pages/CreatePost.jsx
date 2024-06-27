import React, {useState} from 'react'
import axios from "axios";

const CreatePost = () => {
  const [tweetPost,setTweetPost] = useState('');
  const sendPost = (e) => {
    e.preventDefault();
    axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/create`,
      {
        tweetPost,
      }) .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  }
  return (
    <div className="w-screen h-screen">
        CreatePost
        <form className="w-2/3">
          <textarea className="w-full h-40 border border-yellow-500" type = "text" value = {tweetPost} onChange={(e) => setTweetPost(e.target.value)} placeholder="What's on your mind"/>
          <div className="w-full">
            <div className="justify-end flex"> 
              <button className="bg-yellow-400 hover:bg-yellow-500 text-white font-bold py-2 px-4 rounded" type="submit" onClick={sendPost}>Post</button>
              </div>
          </div>
        </form>
    </div>
  )
}

export default CreatePost