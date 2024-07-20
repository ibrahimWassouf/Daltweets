import { useEffect, useState } from "react";
import { Tab, TabGroup, TabList, TabPanels, TabPanel } from "@headlessui/react";
import axios from "axios";
import "../styles/Friends.css";
import FollowRequestTab from "../components/FollowRequestTab";
import FollowersTab from "../components/FollowersTab";
import FollowingTab from "../components/FollowingTab";

function FollowRequests() {
  const user = JSON.parse(localStorage.getItem("user"));
  const username = user.username;
  const [requests, setRequests] = useState([]);
  const [followings, setFollowings] = useState([]);
  const [followers, setFollowers] = useState([]);
  const [accepted, setAccepted] = useState(false);
  const [unfollowed, setUnfollowed] = useState(false);

  const onAccept = async (username, followerName) => {
    const response = await axios.post(
      `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/accept`,
      {
        username,
        followerName,
      }
    );

    setAccepted(true);
  };

  const onRemove = async (username, followerName) => {
    const response = await axios.post(
      `${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/delete`,
      {
        user: username,
        follower: followerName,
      }
    );

    setUnfollowed(true);
  };

  const fetchRequests = async () => {
    await axios
      .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${username}/requests`)
      .then((response) => {
        setRequests(response.data);
        setAccepted(false);
        setUnfollowed(false);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const fetchFollowing = async () => {
    await axios
      .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${username}/following`)
      .then((response) => {
        setFollowings(response.data);
        setAccepted(false);
        setUnfollowed(false);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const fetchFollowers = async () => {
    await axios
      .get(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/followers/${username}/followers`)
      .then((response) => {
        setFollowers(response.data);
        setAccepted(false);
        setUnfollowed(false);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    fetchRequests();
    fetchFollowing();
    fetchFollowers();
  }, [accepted, unfollowed]);

  return (
    <>
      <TabGroup className="mx-auto w-full h-min">
        <TabList className="box-border  m-auto my-5 w-full space-x-4 flex justify-center">
          <div className="shadow-md p-1 flex shrink justify-between *:p-2 rounded-md">
            <div className="flex shrink flex-row items-center *:p-1">
              <Tab className="rounded-full py-1 px-3 text-sm/6 font-semibold focus:outline-none data-[selected]:bg-gold/50 data-[hover]:bg-gold/50 data-[selected]:data-[hover]:bg-gold/50 data-[focus]:outline-1 data-[focus]:outline-white flex flex-row *:p-2 items-center">
                <p>Follow Requests</p>
                <p>{requests.length}</p>
              </Tab>
            </div>
            <div className="flex flex-row items-center *:p-1">
              <Tab className="rounded-full py-1 px-3 text-sm/6 font-semibold focus:outline-none data-[selected]:bg-gold/50 data-[hover]:bg-gold/50 data-[selected]:data-[hover]:bg-gold/50 data-[focus]:outline-1 data-[focus]:outline-white flex flex-row *:p-2 items-center">
                <p>Following</p>
                <p>{followings.length}</p>
              </Tab>
            </div>
            <div className="flex flex-row items-center *:p-1">
              <Tab className="rounded-full py-1 px-3 text-sm/6 font-semibold focus:outline-none data-[selected]:bg-gold/50 data-[hover]:bg-gold/50 data-[selected]:data-[hover]:bg-gold/50 data-[focus]:outline-1 data-[focus]:outline-white flex flex-row *:p-2 items-center">
                <p>Followers</p>
                <p>{followers.length}</p>
              </Tab>
            </div>
          </div>
        </TabList>
        <TabPanels className="box-border w-full h-max">
          <TabPanel className="box-border w-full">
            <FollowRequestTab
              requests={requests}
              onAccept={onAccept}
              onRemove={onRemove}
            />
          </TabPanel>
          <TabPanel>
            <FollowingTab followings={followings} onRemove={onRemove} />
          </TabPanel>
          <TabPanel>
            <FollowersTab followers={followers} onRemove={onRemove} />
          </TabPanel>
        </TabPanels>
      </TabGroup>
    </>
  );
}

export default FollowRequests;
