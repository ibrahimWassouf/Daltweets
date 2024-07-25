import React from 'react'
import { Link } from "react-router-dom";

export default function GroupElement({ group }) {
  return (
    <div className="w-full flex justify-between items-center">
      <Link to={`/groupdetail/${encodeURIComponent(group.name)}`} className="hover:text-yellow-700">{group.name}</Link>
      <p>{group.dateCreated}</p>
      {group.isPublic ? (
        <div className="box-border p-2 bg-green-500 rounded-full">
          <p>Public</p>
        </div>
      ) : (
        <div className="box-border p-2 bg-red-400 rounded-full">
          <p>Private</p>
        </div>
      )}
    </div>
  );
}
