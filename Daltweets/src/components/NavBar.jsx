import React from 'react'
import { FaHome, FaSearch, FaUserCircle, FaUserFriends } from 'react-icons/fa'
import { FaUser } from 'react-icons/fa6'
import { Link } from 'react-router-dom'


const NavBar = () => {
  return (
   <div className='flex justify-center items-center bg-gray-800 h-full px-4 py-2 min-h-screen mr-1 w-1/6 text-xl'>
        <ul className=' text-white font-bold'>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <Link to={"/pages/home"}> 
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
                <a href=""> {/** When add the link change from a to Link component*/}
                    <FaUserFriends className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Friends
                </a>
            </li>
            <li className="mb-20 rounded hover:shadow hover:bg-blue-500 py-2"> 
                <a href=""> {/** When add the link change from a to Link component*/}
                    <FaUserCircle className="inline-block w-6 h-6 mr-2 -mt-2"/> 
                    Profile
                </a>
            </li>
        </ul>
   </div>
  )
}

export default NavBar