import React from 'react'
import ReactTimeAgo from 'react-time-ago';
import TimeAgo from 'javascript-time-ago'

import en from 'javascript-time-ago/locale/en'
import { CommandLineIcon } from '@heroicons/react/16/solid';
import { FaComment, FaRegComment, FaRegHeart } from 'react-icons/fa';
import { CiHeart } from 'react-icons/ci';

TimeAgo.addDefaultLocale(en)


function Post({text,username,dateCreated,...props}) {
  return (
    <div>
    
    <div className="border mb-1 border-black mr-2 w-2/3">
        <div className="pl-2">
          <div>
            <span className="font-bold">{username}</span>
            <span className="pl-1 text-gray-500"> <ReactTimeAgo date={Date.parse(dateCreated) }></ReactTimeAgo>  </span>
          </div>
          <div>
            {text}
          </div>
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

export default Post