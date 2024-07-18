import axios from "axios";
import { useEffect, useState } from "react";

export default function UserList() {
  let admin = JSON.parse(localStorage.getItem("user"));
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/all-users`
        );
        console.log(response.data);
        let userList = response.data.filter(
          (u) => u.username != admin.username
        );
        setUsers(userList);
      } catch (error) {
        console.error("Error get data", error);
      }
    };
    fetchData();
  }, []);
  let handleActivate = (username, idx) => {
    try {
      const response = axios.post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/activate`,
        {
          adminName: admin.username,
          username: username,
        }
      );

      const newUsers = users.map((u, i) => {
        if (i == idx) {
          return { ...u, accountDeleted: false };
        }
        return u;
      });
      setUsers(newUsers);
    } catch (error) {
      console.error("Error on sending Activate request", error);
    }
  };

  let handleDeactivate = (username, idx) => {
    const response = axios
      .post(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/deactivate`, {
        adminName: admin.username,
        username: username,
      })
      .then((response) => {
        if (response.data) users[idx].accountDeleted = true;
        const newUsers = users.map((u, i) => {
          if (i == idx) {
            return { ...u, accountDeleted: true };
          } else {
            return u;
          }
        });

        setUsers(newUsers);
      })
      .catch((error) => {
        console.error("Error on sending Activate request", error);
      });
  };

  let list = users.map((elem, idx) => (
    <li key={idx} className="mx-auto w-full flex justify-between p-2">
      <p className="w-1/3">{elem.username}</p>
      <p className="w-1/3">{elem.email}</p>
      <div className="w-1/3 flex justify-center">
        {elem.accountDeleted ? (
          <button
            onClick={() => handleActivate(elem.username, idx)}
            className="flex w-20 justify-center rounded-md bg-yellow-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
          >
            <p className="text-bold text-grey-700">Activate</p>
          </button>
        ) : (
          <button
            onClick={() => handleDeactivate(elem.username, idx)}
            className="flex w-20 justify-center rounded-md bg-yellow-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
          >
            <p className="text-bold text-grey-700">Deactivate</p>
          </button>
        )}
      </div>
    </li>
  ));

  return (
    <>
      <div className=" box-border mx-auto w-3/6 flex justify-center">
        <ul className="w-full flex flex-col justify-center ">{list}</ul>
      </div>
    </>
  );
}
