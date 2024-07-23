import { useState, useEffect } from "react";
import axios from "axios";
import SearchItem from "../components/SearchItem";

const Search = () => {
  const [activeTab, setActiveTab] = useState("Users");
  const [searchTerm, setSearchTerm] = useState("");
  const [users, setUsers] = useState([]);
  const [followers, setFollowers] = useState([]);
  const [following, setFollowing] = useState([]);
  const [groups, setGroups] = useState([]);
  const [filteredResults, setFilteredResults] = useState([]);
  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    const fetchUsers = async () => {
      const response = await axios.get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/${user.username}/all-users`,
        {
          username: user.username,
        }
      );
      setUsers(response.data);
    };

    const fetchGroups = async () => {
      const response = await axios.get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/all`
      );
      setGroups(response.data);
    };

    const fetchFollowers = async () => {
      const response = await axios.get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${user.username}/followers`
      );

      setFollowers(response.data);
    };

    const fetchFollowing = async () => {
      const response = await axios.get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${user.username}/following`
      );
      setFollowing(response.data);
    };

    fetchFollowers();
    fetchFollowing();
    fetchUsers();
    fetchGroups();
  }, []);

  useEffect(() => {
    // Filter results based on the active tab and search term
    if (activeTab === "Users") {
      setFilteredResults(
        users.filter((user) =>
          user.username.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    } else if (activeTab === "Groups") {
      setFilteredResults(
        groups.filter((group) =>
          group.name.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    } else if (activeTab === "Followers") {
      setFilteredResults(
        followers.filter((user) =>
          user.username.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    } else if (activeTab === "Following") {
      setFilteredResults(
        following.filter((user) =>
          user.username.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    }
  }, [activeTab, searchTerm, users, groups]);

  const handleTabClick = (tabName) => {
    setActiveTab(tabName);
  };

  const isFriend = (username) => {
    const isFollowing =
      following.find((user) => user.username === username) !== undefined;
    const isFollower = followers.find((user) => user.username === username) !== undefined;

    return isFollower || isFollowing;
  };

  return (
    <div className="bg-white in-h-screen text-white">
      <header className="sticky top-0 bg-opacity-80 backdrop-blur-sm border-b border-gray-500 z-10">
        <div className="max-w-3xl mx-auto px-4 py-3 flex items-center">
          <div></div>
          <div className="flex-grow">
            <input
              type="text"
              placeholder="Search Daltweets"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full text-black border border-gray-400 rounded-full py-2 px-4 focus:ring-2 focus:ring-gray-500 focus:border-none"
            />
          </div>
          <div className="w-12 flex justify-end">
            <svg
              className="w-6 h-6 text-gray-400"
              fill="currentColor"
              viewBox="0 0 24 24">
              <path d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
            </svg>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-3xl mx-auto mt-4 px-4">
        {/* Tabs */}
        <div className="flex border-b border-gray-700 mb-4">
          <button
            className={`flex-1 py-3 font-bold ${activeTab === "Users" ? "text-yellow-600 border-b-2 border-yellow-600" : "text-gray-500"}`}
            onClick={() => handleTabClick("Users")}>
            Users
          </button>

          <button
            className={`flex-1 py-3 font-bold ${activeTab === "Followers" ? "text-yellow-600 border-b-2 border-yellow-600" : "text-gray-500"}`}
            onClick={() => handleTabClick("Followers")}>
            Followers
          </button>

          <button
            className={`flex-1 py-3 font-bold ${activeTab === "Following" ? "text-yellow-600 border-b-2 border-yellow-600" : "text-gray-500"}`}
            onClick={() => handleTabClick("Following")}>
            Following
          </button>

          <button
            className={`flex-1 py-3 font-bold ${activeTab === "Groups" ? "text-yellow-600 border-b-2 border-yellow-600" : "text-gray-500"}`}
            onClick={() => handleTabClick("Groups")}>
            Groups
          </button>
        </div>

        {/* Search Results */}
        <div className="space-y-4">
          {filteredResults !== undefined && filteredResults.length > 0 ? (
            filteredResults.map((result) => (
              <div key={result.id} className="border-b text-black border-gray-700 pb-4">
                <div className="flex items-center mb-2">
                  <div>
                    {activeTab !== "Groups" ? (
                      <SearchItem
                        name={result.username}
                        bio={result.bio}
                        route={`profile/${encodeURIComponent(result.username)}`}
                        isFriend={isFriend(result.username)}
                      />
                    ) : (
                      <SearchItem
                        name={result.name}
                        bio={result.description}
                        group={result}
                        route={`groupdetail/${encodeURIComponent(result.name)}`}
                      />
                    )}
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className="flex justify-center">
              <p className="text-black "> No {activeTab}... </p>
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default Search;
