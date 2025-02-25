import React, { useEffect, useState } from "react";
import HomePage from "./HomePage";
import NavBar from "../components/NavBar";
import CreatePost from "./CreatePost";
import Profile from "./Profile";
import AdminPage from "./AdminPage";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Friends from "./Friends";
import Error from "./Error";
import UpdateUser from "./UpdateUser";
import Group from "./Group";
import GroupDetail from "../components/GroupDetail";
import Search from "./Search";
import PostPage from "./PostPage";
import TopicPosts from "./TopicPosts";
const Pages = () => {
  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <>
      {user && user.status !== "PENDING" ? (
        <div className="flex ">
          <NavBar />
          <main className="flex-1 pl-[17.666667%] p-4">
          <Routes>
            <Route path="*" element={<HomePage />}/>
            <Route path="/home" element={<HomePage />} />
            <Route path="/friends" element={<Friends />} />
            <Route path="/create" element={<CreatePost />} />
            <Route path="/profile/:username" element={<Profile />} />
            <Route path="/profile" element={<Profile />}/>
            <Route path="/updateUser" element={<UpdateUser />} />
            <Route path="/admin" element={<AdminPage />} />
            <Route path="/group" element={<Group />} />
            <Route path="/groupdetail/:groupname" element={<GroupDetail />} />
            <Route path="/search" element={<Search />} />
            <Route path="/post/:postId" element={<PostPage />}/>
            <Route path="/topic/:topicname" element={<TopicPosts/>}/>
          </Routes>
          </main>
        </div>
      ) : (
        <Error />
      )}
    </>
  );
};

export default Pages;
