import React, {useState} from 'react'
import axios from "axios";
import { MdEmojiEmotions } from "react-icons/md";
import { FaPlus } from "react-icons/fa";

const CreatePost = () => {
  const [id,setId] = useState(1);
  const [text,setText] = useState('');
  const [dateCreated,setDateCreated] = useState(null);
  const [isDeleted,setIsDeleted] = useState(false);
  const [isEdited,setIsEdited] = useState(false);
  const [topics,setTopics] = useState("");

  const sendPost = (e) => {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem('user'));
    const formData = {
      id,
      text,
      user,
      dateCreated,
      isDeleted,
      isEdited,
    }
    axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/create`,
      formData
    ) .then((response) => {

        let temp = "";
        let check = true;
        for ( let i = 0 ; i < topics.length && check ; ++i ) {
          if ( topics[i] == '#' || i == topics.length - 1) {
            if ( i == topics.length - 1 ) temp += topics[i];
            if ( temp ) {
              console.log(temp);
              let postId = response.data.postID.toString();
              const topicData = {
                topicname: temp,
                postId: postId,
              }
              axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/createPostTopic`,topicData)
              .then((responseTopic) => {
                console.log(responseTopic);
              })
              .catch((error) => {
                console.log(error);
                alert('Topic not sent');
                check = false;
              })
            }
            temp = "";
          } else {
            temp += topics[i];
          }
        }
        if ( check ) alert('Post sent success');
        console.log(response);
      })
      .catch((error) => {
        alert('Post not sent');
        console.log(error);
      });
  }
  return (
      <div className="min-h-screen h-full bg-gray-50 flex items-center flex-1 justify-center content-center">
        <div className="container content-center rounded-lg bg-white w-5/6 h-5/6">
          <div className="w-5/6 text-xl text-gray-900 mx-auto">
            <div className="mb-14 mt-5 text-3xl font-bold ">
              Create Post
            </div>
            <form className="h-full">
              <input className="w-full border mb-2 rounded-2xl indent-2 shadow-2xl border-gray-300"
                     type="text" 
                     value={topics} 
                     onChange={(e) => setTopics(e.target.value)}
                     placeholder="Add topics"
              >

              </input>
              <textarea className="w-full text-xl h-80 flex rounded-md pl-2 font-normal border mb-2 resize-none shadow-2xl shadow-gray-400"
                        type="text" value={text} onChange={(e) => setText(e.target.value)}
                        placeholder="What's on your mind?"/>
              <div className="w-full">
                <div className="justify-end flex">
                  <FaPlus className="mt-1 mb-1 inline-block w-11 h-9" /><MdEmojiEmotions className="mr-1 mt-1 mb-1 inline-block w-11 h-9" />
                  <button className="bg-gold hover:bg-black hover:text-gold text-black font-medium py-1 px-4 rounded-md"
                          type="submit" onClick={sendPost}>Post
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
  )
}

export default CreatePost