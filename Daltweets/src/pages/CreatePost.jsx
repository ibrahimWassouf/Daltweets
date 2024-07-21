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
        alert('Post sent success');
        console.log(response);
      })
      .catch((error) => {
        alert('Post not sent');
        console.log(error);
      });
  }
  return (
      <div className="h-screen bg-gray-50 flex items-center flex-1 justify-center content-center">
        <div className="container content-center rounded-lg bg-white w-5/6 h-5/6">
          <div className="w-5/6 font-bold text-xl text-gray-900 mx-auto">
            <div className="mb-20 mt-5 text-3xl">
              Create Post
            </div>
            <form className="h-full">
              <textarea className="w-full text-xl h-80 flex rounded-md pl-2 font-normal border mb-2 border-black resize-none"
                        type="text" value={text} onChange={(e) => setText(e.target.value)}
                        placeholder="What's on your mind?"/>
              <div className="w-full">
                <div className="justify-end flex">
                  <FaPlus className="mt-1 mb-1 inline-block w-11 h-9" /><MdEmojiEmotions className="mr-1 mt-1 mb-1 inline-block w-11 h-9" />
                  <button className="bg-yellow-400 hover:bg-gold text-black font-medium py-1 px-4 rounded-md"
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