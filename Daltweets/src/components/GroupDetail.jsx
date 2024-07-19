import React, {useState,useEffect} from 'react'
import { useParams } from 'react-router-dom'
import Post from './Post';
import axios from 'axios';

const GroupDetail = () => {
  const { groupname } = useParams();
  const [group,setGroup] = useState(null);
  const [posts,setPosts] = useState([]);
  const [admins,setAdmins] = useState([]);
  const [members,setMembers] = useState([]);
  useEffect(() => {
    const fetchGroup = async () => {
      const name = JSON.parse(localStorage.getItem('user')).username;
      try {
        const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/${groupname}/groupPosts`);
        setPosts(response.data);
        console.log(response.data);
        
        const response1 = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/${groupname}/admins`);
        setAdmins(response1.data);
        console.log(response1.data);
        
        const response2 = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/${groupname}/members`);
        setMembers(response2.data);
        console.log(response2.data);
      } catch (error ) {
        console.error('Error fetching groups',error);
      }
    };
    fetchGroup();
    
  },[])
  return (   
    <div className="w-screen min-h-screen flex mt-4 ml-2">
        <div className="w-3/4">
          {posts
            ? posts.map(
              (post, index) => (
                //console.log(post),
                (
                  <Post
                    key={index}
                    username={post.user.username}
                    dateCreated={post.dateCreated}
                    {...post}
                  />
                )
              ),
            )
            : (console.log(posts), (<p> Loading posts .... </p>))}
        </div>

        <div className="ml-14 border-black border w-1/6 h-svh">
            <div className="justify-center flex">
                <h3 className="font-bold"> Admins </h3>
            </div>
            <div className="ml-4">
                    {admins
                    ? admins.map(
                    (admin, index) => (
                        (
                            <div>
                                {admin.username}    
                            </div>
                        )
                    ),
                    )
                    : (console.log(admins), (<p> no admins .... </p>))}
            </div>
            <div className="justify-center flex">
                <h3 className="font-bold"> Members </h3>
            </div>
            <div className="ml-4">
                {members
                ? members.map(
                (member, index) => (
                    (
                        <div>
                            {member.username}    
                        </div>
                    )
                ),
                )
                : (console.log(members), (<p> no members .... </p>))}
            </div>
        </div>
    </div>
  )
}

export default GroupDetail

