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
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const updateUser = (e) => {
    e.preventDefault();

    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

    if (password && !passwordRegex.test(password)) {
      setError(
        "Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character"
      );
      return;
    }

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
      ...(password && { password }),
    };

    axios
      .put(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/update`, formData)
      .then((response) => {
        alert("Update success");
        localStorage.setItem("user", JSON.stringify(response.data));
        console.log(response);
      })
      .catch((error) => {
        setError("Update failed: " + error.response.data.message);
        console.log(error);
      });
  };

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
        Update User
      </h2>
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-md">
        <form onSubmit={updateUser} className="space-y-6">
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Username
            </label>
            <div>
              <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUserName(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          <div>
            <label
              htmlFor="email"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Email
            </label>
            <div>
              <input
                id="email"
                type="email"
                value={email}
                readOnly
                className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 bg-gray-200 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          {user.role === "SUPERADMIN" && (
            <div>
              <label
                htmlFor="role"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Role
              </label>
              <div>
                <input
                  id="role"
                  type="text"
                  value={role}
                  onChange={(e) => setRole(e.target.value.toUpperCase())}
                  className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>
          )}
          <div>
            <label
              htmlFor="status"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Status
            </label>
            <div>
              <select
                id="status"
                value={status}
                onChange={(e) => setStatus(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              >
                <option value="ONLINE">Online</option>
                <option value="OFFLINE">Offline</option>
                <option value="AWAY">Away</option>
                <option value="BUSY">Busy</option>
              </select>
            </div>
          </div>
          <div>
            <label
              htmlFor="bio"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Bio
            </label>
            <div>
              <textarea
                id="bio"
                value={bio}
                onChange={(e) => setBio(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 h-36"
              ></textarea>
            </div>
          </div>
          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium leading-6 text-gray-900"
            >
              Password
            </label>
            <div>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
              />
            </div>
          </div>
          <div>
            <button
              type="submit"
              className="w-full flex justify-center rounded-md bg-indigo-600 py-2 px-4 text-sm font-medium text-white shadow-sm hover:bg-indigo-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Submit
            </button>
          </div>
        </form>
        {error && <div className="mt-3 text-red-600 text-sm">{error}</div>}
      </div>
    </div>
  );
};

export default UpdateUser;
