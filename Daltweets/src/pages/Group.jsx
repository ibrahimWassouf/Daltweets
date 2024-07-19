import axios from "axios";
import { useState, useEffect } from "react";
import GroupDisplay from "../components/GroupDisplay";
import CreateGroup from "./CreateGroup";
import { IoIosAddCircle } from "react-icons/io";

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
  return (
    <div className="container">
      <div className="mt-5">
        <span>
          <button className="text-white bg-blue-700 hover:bg-blue-800 focus:outline-none font-medium text-sm rounded-lg px-5 py-2.5 text-center flex items-center justify-center"
          onClick={() => setCreateGroup(true)}
          >
            <IoIosAddCircle className="mr-1 text-2xl"/> Create a group
          </button>
          <CreateGroup isVisible = {createGroup} onClose={() => setCreateGroup(false)}/>
        </span>


        <ul>
          {groups ? 
          groups.map((group,index) => {
            console.log(group);
            return (
              <div key={index}>
                <GroupDisplay groupname={group.name} Public={group.isPublic}/>
              </div>
            );
          }) : "Error"}

        </ul>
      </div>
    </div>
    
  )
}

export default Group