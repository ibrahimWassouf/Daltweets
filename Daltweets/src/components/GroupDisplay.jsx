import React from 'react'
import { Link } from 'react-router-dom'

function GroupDisplay({groupname, Public}) {
  let statusColor;
  let group_status;
  switch(Public) {
    case true:
        statusColor =  "bg-green-500 text-white";
        group_status = "Public Group";
        break;
    case false:
        statusColor =  "bg-red-500 text-white";
        group_status = "Private Group";
        break;
  }
  
  console.log(group_status);
  console.log(Public);
  return (
    <div className="justify-center flex">
        <div className="border mb-5 border-black w-1/2 rounded-md ">
            <div className="pl-2">
            <div>
                <div className="pt-1">
                <span className={`rounded-md pl-2 pr-2 pb-1 ${statusColor}`}>{group_status}</span>
                </div>
            </div>
            <div className="flex justify-center">
                <Link to={`/groupdetail/${encodeURIComponent(groupname)}`} className="font-bold">{groupname}</Link>
            </div>
            </div>
        </div>  
    </div>
  )
}

export default GroupDisplay