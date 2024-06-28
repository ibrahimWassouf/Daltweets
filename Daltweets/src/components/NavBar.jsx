import React from 'react'
import { CiLogout } from 'react-icons/ci'
import { FaHome, FaSearch, FaUserCircle, FaUserFriends } from 'react-icons/fa'
import { IoCreateOutline } from 'react-icons/io5'
import { Link } from 'react-router-dom'


const NavBar = () => {
    const handleLogout = () => {
        localStorage.clear();
    };
  return (
   <div className='min-h-screen flex justify-center items-center bg-gray-800 px-4 py-2 mr-1 w-1/6 text-xl'>
        <ul className=' text-white font-bold'>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <Link to={"/home"}> 
                    <FaHome className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Home
                </Link>
            </li>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <a href=""> {/** When add the link change from a to Link component*/}
                    <FaSearch className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Search
                </a>
            </li>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <Link to="/create"> {/** When add the link change from a to Link component*/}
                    <IoCreateOutline className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Create
                </Link>
            </li>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <Link to="/friends"> {/** When add the link change from a to Link component*/}
                    <FaUserFriends className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Friends
                </Link>
            </li>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <Link to="/profile"> {/** When add the link change from a to Link component*/}
                    <FaUserCircle className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Profile
                </Link>
            </li>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <Link to="/login" onClick={handleLogout}> {/** When add the link change from a to Link component*/}
                    <CiLogout className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Logout
                </Link>
            </li>
        </ul>
   </div>
  )
}

export default NavBar