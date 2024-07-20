import axios from "axios";
import { useEffect, useState } from "react";
import GroupElement from "./GroupElement";

export default function GroupList() {
  let admin = JSON.parse(localStorage.getItem("user"));
  const [groups, setGroups] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/all`
        );

        setGroups(response.data);
      } catch (error) {
        console.error("Error get data", error);
      }
    };
    fetchData();
  }, []);

  let list = groups.map((elem, idx) => (
    <li key={idx} className="mx-auto w-full flex justify-between p-2">
      <GroupElement group={elem} />
    </li>
  ));
  return (
    <>
      <div className="box-border flex flex-row justify-between mx-auto w-3/6 pr-5 font-bold m-2">
        <p className="w-1/3">Group Name</p>
        <p className="w-1/3 text-center">Date Created</p>
        <p className="w-1/3 text-right">Access</p>
      </div>
      <div className=" box-border mx-auto w-3/6 flex justify-center">
        <ul className="w-full flex flex-col justify-center ">{list}</ul>
      </div>
    </>
  );
}
