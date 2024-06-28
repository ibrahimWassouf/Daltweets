import React, { useState } from 'react'
import axios from "axios";

const UpdateUser = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    const [id,setId] = useState(user.id);
    const [bio,setBio] = useState(user.bio);
    const [username,setUserName] = useState(user.username);
    const [email,setEmail] = useState(user.email);
    const [dateCreated,setDateCreated] = useState(user.dateCreated);
    const [accountDeleted,setAccountDeleted] = useState(user.accountDeleted);
    const [role,setRole] = useState(user.role);
    const [status,setStatus] = useState(user.status);
  
    const updateuser = (e) => {
      e.preventDefault();
      const formData = {
        id,
        bio: (bio === "") ? user.bio : bio,
        username: (username === "") ? user.username : username,
        email: (email === "") ? user.email : email,
        dateCreated,
        accountDeleted: (accountDeleted === "") ? user.accountDeleted : accountDeleted,
        role: (role === "") ? user.role : role,
        status: (status === "") ? user.status : status,
      }
      axios.put(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/update`,
        formData
      ) .then((response) => {
          alert('Update success');
          localStorage.setItem('user',JSON.stringify(response.data));
          console.log(response);
        })
        .catch((error) => {
          alert('Update fail');
          console.log(error);
        });
    }
    return (
      <div className="w-screen h-screen">
          Update User
          <form onSubmit={updateuser} style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' }}>
            
            <input type="text" name="bio" placeholder="Bio" onChange={(e) => setBio(e.target.value) } style={{ padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}/>
            <input type="text" name="username" placeholder="Name" onChange={(e) => setUserName(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}/>
            <input type="text" name="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}/>
            <input type="text" name="role" placeholder="Role" onChange={(e) => setRole(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}/>
            <input type="text" name="status" placeholder="Status" onChange={(e) => setStatus(e.target.value)} style={{ padding: '10px', borderRadius: '5px', border: '1px solid #ccc' }}/>
            <button type="submit" style={{ padding: '10px', borderRadius: '5px', border: 'none', backgroundColor: '#007BFF', color: 'white', cursor: 'pointer' }}>Submit</button>
          </form>
      </div>
    )
}

export default UpdateUser