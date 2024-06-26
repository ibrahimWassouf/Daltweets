import React from 'react'

function Post(props) {
  return (
    <div>
        <li>
            <div> props.text </div>
            <div> props.user.username </div>
            <div> props.dateCreated </div>
        </li>
    </div>
  )
}

export default Post