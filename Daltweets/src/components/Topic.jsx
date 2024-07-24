import React from 'react'
import { Link } from 'react-router-dom'

function Topic({topicname}) {
  return (
    <div>
        <Link to = {`/topic/${encodeURIComponent(topicname)}`} className="pr-1.5 text-sky-500 py-1" onClick="refresh">
          #{topicname}
        </Link>
    </div>
  )
}

export default Topic