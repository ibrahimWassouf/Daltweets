import React, { useState } from "react";
import axios from "axios";

const UpdateUser = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  const [id, setId] = useState(user.id);
  const [bio, setBio] = useState(user.bio);
  const [username, setUserName] = useState(user.username);
  const [email, setEmail] = useState(user.email);
  const [dateCreated, setDateCreated] = useState(user.dateCreated);
  const [accountDeleted, setAccountDeleted] = useState(user.accountDeleted);
  const [role, setRole] = useState(user.role);
  const [status, setStatus] = useState(user.status);

  const updateuser = (e) => {
    e.preventDefault();
    const formData = {
      id,
      bio: bio === "" ? user.bio : bio,
      username: username === "" ? user.username : username,
      email: email === "" ? user.email : email,
      dateCreated,
      accountDeleted:
        accountDeleted === "" ? user.accountDeleted : accountDeleted,
      role: role === "" ? user.role : role,
      status: status === "" ? user.status : status,
    };
    axios
      .put(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/update`, formData)
      .then((response) => {
        alert("Update success");
        localStorage.setItem("user", JSON.stringify(response.data));
        console.log(response);
      })
      .catch((error) => {
        alert("Update fail");
        console.log(error);
      });
  };

  return (
    <div className="w-screen h-screen ">
      <div className="justify-center flex h-12 ">Update User</div>
      <div className="justify-center flex ">
        <form onSubmit={updateuser} className="flex flex-col gap-4 w-72">
          <input
            type="text"
            name="username"
            defaultValue={user.username}
            placeholder="Name"
            onChange={(e) => setUserName(e.target.value)}
            className="p-3 rounded-md border-slate-400 border-2"
          />
          <input
            type="text"
            name="email"
            defaultValue={user.email}
            placeholder="Email"
            onChange={(e) => setEmail(e.target.value)}
            className="p-3 rounded-md border-slate-400 border-2"
          />
          {user.role === "SUPERADMIN" && (
            <input
              type="text"
              name="role"
              defaultValue={user.role}
              placeholder="Role"
              onChange={(e) => setRole(e.target.value.toUpperCase())}
              className="p-3 rounded-md border-slate-400 border-2"
            />
          )}
          <input
            type="text"
            name="status"
            defaultValue={user.status}
            placeholder="Status"
            onChange={(e) => setStatus(e.target.value.toUpperCase())}
            className="p-3 rounded-md border-slate-400 border-2"
          />
          <textarea
            name="bio"
            defaultValue={user.bio}
            placeholder="Bio"
            onChange={(e) => setBio(e.target.value)}
            className="p-3 rounded-md border-slate-400 border-2 h-36 "
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
  );
};

export default UpdateUser;
