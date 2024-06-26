import React from 'react'

function Post({text,username,dateCreated,...props}) {
  return (
    <div>
        <li>
          <div> {text} </div>
        </li>
        <li>
          <div> {username}</div>
        </li>
        <li>
          <div> {dateCreated} </div>
        </li>
        <li>
          <div> </div>
        </li>
    </div>
  );
}

export default Post