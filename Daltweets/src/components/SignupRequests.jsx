import axios from "axios";
import { useEffect, useState } from "react";

export default function SignupRequests() {
  const [requests, setRequests] = useState([]);
  let admin = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/signup-requests`
        );

        setRequests(response.data);
      } catch (error) {
        console.error("Error get data", error);
      }
    };
    fetchData();
  }, []);

  let handleAccept = async (username) => {
    try {
      await axios.post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/changeStatus`,
        {
          adminName: admin.username,
          username: username,
          status: "OFFLINE",
        }
      );
      let newList = requests.filter((u) => u.username != username);
      setRequests(newList);
    } catch (error) {
      console.error(error);
    }
  };

  let handleReject = async (username) => {
    try {
      await axios.post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/changeStatus`,
        {
          adminName: admin.username,
          username: username,
          status: "OFFLINE",
        }
      );

      await axios.post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/deactivate`,
        {
          adminName: admin.username,
          username: username,
        }
      );

      let newList = requests.filter((u) => u.username != username);
      setRequests(newList);
    } catch (error) {
      console.error(error);
    }
  };

  let list = requests.map((elem, idx) => (
    <li key={idx} className="mx-auto w-full flex justify-between p-2">
      <p className="w-1/3">{elem.username}</p>
      <p className="w-1/3">{elem.email}</p>
      <div className="w-1/3 flex justify-around">
        <button
          onClick={() => handleAccept(elem.username)}
          className="flex w-20 justify-center rounded-md bg-yellow-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          <p className="text-bold text-grey-700">Accept</p>
        </button>
        <button
          onClick={() => handleReject(elem.username)}
          className="flex w-20 justify-center rounded-md bg-yellow-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          <p className="text-bold text-grey-700">Reject</p>
        </button>
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
