import { useEffect, useState } from "react";
import axios from "axios";
import { GoDotFill } from "react-icons/go";
import { FaGear } from "react-icons/fa6";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import csBanner from '../../public/cs-banner.jpg';
import favicon from '../../public/favicon.ico';
import ReactTimeAgo from "react-time-ago";
import Post from "../components/Post";
import GroupElement from "../components/GroupElement";

function Profile() {
    const location = useLocation();
    const { isFriend } = location.state || {};
    const loggedInUser = JSON.parse(localStorage.getItem("user"));
    const { username } = useParams() || loggedInUser.username;
    const [backendUser, setBackendUser] = useState({});
    const isLoggedInUser = !backendUser || backendUser.username == loggedInUser.username;
    const user = backendUser ? backendUser : loggedInUser;
    const [activeTab, setActiveTab] = useState('posts');
    const [groups, setGroups] = useState([]);
    const [posts, setPosts] = useState([]);

    let statusColor = "";
    switch (user.status) {
        case "ONLINE":
            statusColor = "green";
            break;
        case "OFFLINE":
            statusColor = "red";
            break;
        case "DEACTIVATED":
            statusColor = "grey";
            break;
        default:
            statusColor = "yellow";
    }

    useEffect(() => {
        getProfile();
        fetchGroups();
        fetchPosts();
    }, [username]);

    const getProfile = async () => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/${username}/profile`);
            setBackendUser(response.data);
        } catch (error) {
            console.error("Error fetching profile.", error);
        }
    };

    const fetchGroups = async () => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/${username}/groups`);
            setGroups(response.data);
        } catch (error) {
            console.error("Error fetching groups.", error);
        }
    };

    const fetchPosts = async () => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/post/${username}/posts`);
            setPosts(response.data);
        } catch (error) {
            console.error("Error fetching posts.", error);
        }
    };

    let navigate = useNavigate();
    const routeChange = () => {
        let path = "/updateUser";
        navigate(path);
    }

    return (
        <div className="container rounded mt-3 mb-3 mr-3 ml-3">
            <div className="grid">
                <div className="relative my-2 mx-2 border-gray-300 border-2 bg-white rounded-lg">
                    <div>
                        <img src={csBanner} alt="" className="w-full h-52 object-cover" />
                    </div>
                    <div className="absolute top-36 left-5">
                        <img src={favicon} alt="" className="w-24 h-24 border-gray-200 border-4 rounded-full" />
                    </div>
                    <div className="grid-cols-1 mt-8 px-3">
                        <div className="font-bold text-3xl">{user.username}
                            {isLoggedInUser &&
                                <button className="bg-blue-300 hover:bg-yellow-200 rounded-full ml-1 py-0 px-2 text-base" onClick={routeChange}>
                                    <FaGear className="mr-1 inline-block mb-1" />Edit
                                </button>}
                        </div>
                        <div className="text-base">{user.email}</div>
                        {(isFriend || isLoggedInUser) &&
                            <div className="text-base italic flex">{user.status}<GoDotFill color={statusColor} className="flex items-center justify-center" /></div>
                        }
                    </div>
                    <div className="grid-cols-1 border-right">
                        <div className="p-3 py-5">
                            <div className="row mx-2">
                                <div className="text-2xl">
                                    <h2 className="font-bold">About</h2>
                                </div>
                                <div className="col-md-12">
                                    <p>{user && user.bio?.length > 0 ? user.bio : "No Bio"}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="mx-2 my-2 border-gray-300 border-2 bg-white rounded-lg">
                    <div className="flex border-b border-gray-300">
                    <button
                            className={`flex-1 py-2 px-4 text-center font-semibold ${activeTab === 'posts' ? 'text-yellow-600 border-b-2 border-yellow-600' : 'text-gray-500 hover:text-yellow-700'}`}
                            onClick={() => setActiveTab('posts')}
                        >
                            Posts
                        </button>
                        <button
                            className={`flex-1 py-2 px-4 text-center font-semibold ${activeTab === 'groups' ? 'text-yellow-600 border-b-2 border-yellow-600' : 'text-gray-500 hover:text-yellow-700'}`}
                            onClick={() => setActiveTab('groups')}
                        >
                            Groups
                        </button>
                    </div>
                    <div className="p-4">
                    {activeTab === 'groups' && (
                    <ul className="divide-y divide-gray-200">
                        { groups && groups.length > 0 ? groups.map((group, index) => (
                            <li key={index} className="py-2">
                                <GroupElement group={group} />
                            </li> 
                        )) : <p className="flex justify-center text-gray-700">No Groups...</p>}
                    </ul>
                    )} 
                    { activeTab ==='posts' &&
                    <ul className="divide-y divide-gray-200">
                        {posts && posts.length > 0 ? posts.map((post, index) => (
                            <li key={index} className="py-4">
                                <Post
                                    username={post.creator}
                                    dateCreated={post.dateCreated}
                                    commentCount={post.commentCount}
                                    postId = {post.id}
                                    {...post}
                                />
                            </li>
                        )): 
                        <p className="flex justify-center text-gray-700">No Posts...</p>}
                    </ul>
                }
                </div>
            </div>
        </div>
    </div>
    );
}

export default Profile;
