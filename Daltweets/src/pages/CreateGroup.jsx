import axios from 'axios';
import React, { useState } from 'react'
import { IoIosCloseCircleOutline } from 'react-icons/io';

const CreateGroup = ({isVisible, onClose}) => {
  if ( !isVisible ) return null;
  const user = JSON.parse(localStorage.getItem("user"));
  const [id, setId] = useState(1);
  const [name, setName] = useState("");
  const [dateCreated,setDateCreated] = useState(null);
  const [isDeleted,setIsDeleted] = useState(false);
  const [isPublic,setIsPublic] = useState(true);
  const createGroup = (e) => {
     e.preventDefault();
     const formData = {
        id,
        name: name,
        dateCreated: dateCreated,
        isDeleted,
        isPublic: isPublic,
     };
     axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/create`, formData)
     .then((response) => {
        alert("Create group success");
        console.log(response);
      })
      .catch((error) => {
        alert("Create fail");
        console.log(error);
      });
  };
  return (
    <div className="fixed inset-0 bg-black bg-opacity-25 backdrop-blur-sm flex justify-center items-center">
        <div className="w-[400px]">
            
            <div className="bg-white p-2 rounded-2xl flex flex-col h-[400px] w-[400px]">
                <div className="text-white text-xl place-self-end ">
                    <button className="text-black text-3xl" onClick={() => onClose()}>
                    <IoIosCloseCircleOutline />
                    </button>
                </div>
                <div className="flex-row content-center justify-center h-[300px]">
                    <div className="justify-center flex mb-5">
                        <h1 className="font-bold">Create group</h1>
                    </div>
                    <div className="justify-center flex">
                            
                        <form onSubmit={createGroup} className="flex flex-col gap-4 w-72">
                            <input
                                type="text"
                                name="name"
                                placeholder="Name your group"
                                onChange={(e) => setName(e.target.value)}
                                className="p-3 rounded-md border-slate-400 border-2"
                            />
                            <input
                                type="text"
                                name="isPublic"
                                placeholder="Privacy"
                                onChange={(e) => setIsPublic(e.target.value)}
                                className="p-3 rounded-md border-slate-400 border-2"
                            />
                            <button
                                type="submit"
                                className="px-4 py-2 rounded bg-black text-gold font-bold cursor-pointer border-2 border-black"
                            >
                                Submit
                            </button>
                        </form>
                    </div>
                </div>    
            </div>
        </div>    
    </div>
  )
}

export default CreateGroup