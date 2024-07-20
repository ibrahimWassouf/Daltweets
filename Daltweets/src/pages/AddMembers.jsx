import React, { useState } from 'react'
import axios from 'axios';
import { IoIosCloseCircleOutline } from 'react-icons/io';

const AddMembers = ({isVisible, onClose,groupName,isAdmin}) => {
  if ( !isVisible ) return null;  
  const [userName, setUserName] = useState("");
  const addUser = (e) => {
    e.preventDefault();
    const formData = {
        userName: userName,
        groupName: groupName,
        isAdmin: isAdmin,
    };
    axios.post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/adduser`, formData)
    .then((response) => {
        alert("Add member to group success");
        window.location.reload();
        
        console.log(response);
    })
        .catch((error) => {
        alert("Add fail");
        console.log(error);
        window.location.reload();
    });
    
  }
  return (
    <div className="fixed inset-0 bg-black bg-opacity-25 backdrop-blur-sm flex justify-center items-center">
        <div className="w-[300px]">
            
            <div className="bg-white p-2 rounded-2xl flex flex-col h-[250px] w-[320px]">
                <div className="text-white text-xl place-self-end ">
                    <button className="text-black text-3xl" onClick={() => onClose()}>
                    <IoIosCloseCircleOutline />
                    </button>
                </div>
                <div className="flex-row content-center justify-center h-[100px]">
                    <div className="justify-center flex mb-5">
                        { isAdmin  ? <h1 className="font-bold">Add admins</h1> : <h1 className="font-bold">Add members</h1>}
                    </div>
                    <div className="justify-center flex">    
                        <form onSubmit={addUser} className="flex flex-col gap-4 w-72">
                            <input
                                type="text"
                                name="userName"
                                placeholder="username to add"
                                onChange={(e) => setUserName(e.target.value)}
                                className="p-3 rounded-md border-slate-400 border-2"
                            />
                            <button
                                type="submit"
                                className="px-4 py-2 rounded bg-black text-gold font-bold cursor-pointer border-2 border-black hover:bg-gray-500"
                                >
                                    Add
                            </button>
                        </form>
                    </div>
                </div>    
            </div>
        </div>    
    </div>

  )
}

export default AddMembers