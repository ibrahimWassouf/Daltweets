import React from 'react'

function Topic({topicname}) {
  return (
    <div>
        <div className="pr-1.5 text-sky-500">#{topicname}</div>
    </div>
  )
}

export default Topic