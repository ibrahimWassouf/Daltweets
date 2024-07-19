import axios from "axios";
import { useState, useEffect } from "react";
import GroupDisplay from "../components/GroupDisplay";

const Group = () => {
  const [groups,setGroups] = useState([]);
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
      <h2>Groups</h2>
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
    
  )
}

export default Group