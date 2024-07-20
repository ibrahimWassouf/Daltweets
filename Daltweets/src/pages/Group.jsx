import axios from "axios";
import { useState, useEffect } from "react";
import GroupDisplay from "../components/GroupDisplay";
import CreateGroup from "./CreateGroup";
import { IoIosAddCircle } from "react-icons/io";
import GroupElement from "../components/GroupElement";

const Group = () => {
  const [groups,setGroups] = useState([]);
  const [createGroup,setCreateGroup] = useState(false);
  useEffect(() => {
    const fetchGroups = async () => {
      const name = JSON.parse(localStorage.getItem('user')).username;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/${name}/groups`);
        setGroups(response.data);
        console.log(response.data);
      } catch (error ) {
        console.error('Error fetching groups',error);
      }
    };
    fetchGroups();
    
  },[])
  let list = groups.map((elem, idx) => (
    <li key={idx} className="mx-auto w-full flex justify-between p-2">
      <GroupElement group={elem} />
    </li>
  ));
  return (
    <div className="container">
      <div className="mt-5">
        <span className="flex justify-between mx-auto w-3/6 pr-5 font-bold m-2">
          <button className="text-white bg-gold-dark hover:bg-black hover:text-gold focus:outline-none font-medium text-sm rounded-lg px-5 py-2.5 text-center flex items-center justify-center"
          onClick={() => setCreateGroup(true)}
          >
            <IoIosAddCircle className="mr-1 text-2xl"/> Create a group
          </button>
          <CreateGroup isVisible = {createGroup} onClose={() => setCreateGroup(false)}/>
        </span>


        <div className="box-border flex flex-row justify-between mx-auto w-3/6 pr-5 font-bold m-2">
          <p className="w-1/3">Group Name</p>
          <p className="w-1/3 text-center">Date Created</p>
          <p className="w-1/3 text-right">Access</p>
        </div>
        <div className=" box-border mx-auto w-3/6 flex justify-center">
          <ul className="w-full flex flex-col justify-center ">{list}</ul>
        </div>
      </div>
    </div>
    
  )
}

export default Group