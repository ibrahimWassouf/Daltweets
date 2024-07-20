import React, {useState} from 'react'
import axios from "axios";

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
    <div className="h-screen flex items-center flex-1 justify-center content-center py-12 px-6">
        <div className="w-3/4 font-bold leading-9 text-gray-900">
        Create Post
        <form>
          <textarea className="w-full rounded-md pl-2 font-normal h-40 border border-yellow-500 resize-none" type="text" value = {text} onChange={(e) => setText(e.target.value)} placeholder="What's on your mind?"/>
          <div className="w-full">
            <div className="justify-end flex">
              <button className="bg-yellow-400 hover:bg-gold text-black font-medium py-1 px-4 rounded-md" type="submit" onClick={sendPost}>Post</button>
              </div>
          </div>
        </form>
        </div>
    </div>
  )
}

export default CreatePost