import React from "react";
import ReactTimeAgo from "react-time-ago";
import TimeAgo from "javascript-time-ago";

import en from "javascript-time-ago/locale/en";
import { FaRegComment, FaRegHeart } from "react-icons/fa";

TimeAgo.addDefaultLocale(en);

function Post({ text, username, dateCreated, ...props }) {
  return (
    <div>
      <div className="border mb-1 border-black w-full">
        <div className="pl-2">
          <div>
            <span className="font-bold">{username}</span>
            <span className="pl-1 text-gray-500">
              {" "}
              <ReactTimeAgo date={Date.parse(dateCreated)}></ReactTimeAgo>{" "}
            </span>
          </div>
          <div>{text}</div>
          <div className="flex justify-around text-gray-500">
            <button className="flex">
              <FaRegHeart />
              <span>0</span>
            </button>
            <button className="flex">
              <FaRegComment />
              <span>0</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Post;
