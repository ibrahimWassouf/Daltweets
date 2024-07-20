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
  const [description,setDescription] = useState("");
  const createGroup = (e) => {
     e.preventDefault();
     const user = JSON.parse(localStorage.getItem("user"));
     if ( name == "" || isPublic == "" ) {
        alert('Please fill in required fields');
        return ;
     }
     const formData = {
        name: name,
        dateCreated: dateCreated,
        isDeleted,
        isPublic: isPublic,
     };
     axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/create`, formData)
     .then((response) => {
        alert("Create group success");
        console.log(response);

        const groupmemberData = {
            userName: user.username,
            groupName: name,
            isAdmin: true,
        };
        
        axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/adduser`, groupmemberData)
        .then((response) => {
            alert("Add member to group success");
            console.log(response);
        })
            .catch((error) => {
            alert("Add fail");
            console.log(error);
        });
      })
      .catch((error) => {
        alert("Create fail");
        console.log(error);
      });
  };
  return (
    <div className="fixed inset-0 bg-black bg-opacity-25 backdrop-blur-sm flex justify-center items-center">
        <div className="w-[500px]">
            
            <div className="bg-white p-2 rounded-2xl flex flex-col h-[500px] w-[500px]">
                <div className="text-white text-xl place-self-end ">
                    <button className="text-black text-3xl" onClick={() => onClose()}>
                    <IoIosCloseCircleOutline />
                    </button>
                </div>
                <div className="flex-row content-center justify-center h-[350px]">
                    <div className="justify-center flex mb-5">
                        <h1 className="font-bold">Create group</h1>
                    </div>
                    <div className="justify-center flex">
                            
                        <form onSubmit={(e) => {
                            createGroup(e);
                        }} className="flex flex-col gap-4 w-72">
                            <input
                                type="text"
                                name="name"
                                placeholder="Name your group"
                                onChange={(e) => setName(e.target.value)}
                                className="p-3 rounded-md border-slate-400 border-2"
                            />
                            <select name="isPublic" onChange={(e) => setIsPublic(e.target.value)} className="p-3 rounded-md border-slate-400 border-2 text-slate-400">
                                <option value = "" >Choose privacy</option>
                                <option value="true" className="text-black">Public</option>
                                <option value="false" className="text-black">Private</option>
                            </select>
                            <textarea
                                name="descriptiom"
                                placeholder="Add description for the group"
                                onChange={(e) => setDescription(e.target.value)}
                                className="p-3 rounded-md border-slate-400 border-2 h-36 "
                            />
                            <button
                                type="submit"
                                className="px-4 py-2 rounded bg-black text-gold font-bold cursor-pointer border-2 border-black hover:bg-gray-500"
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