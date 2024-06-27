import { useEffect, useState } from "react";
import axios from "axios";
import UserDisplay from "./UserDisplay";

function RecommendedUsers() {
  const [list, setList] = useState([]);
  const [fetchError, setFetchError] = useState(false);

  let user = JSON.parse(localStorage.getItem("user"));
  console.log(user);
  let fetch = () => {
    axios
      .post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/user/recommended-users`,
        {
          username: user.username,
        },
      )
      .then((response) => {
        console.log(response);
        setList([...response.data]);
      })
      .catch((error) => {
        console.log(error);
        setFetchError(true);
      });
  };

  useEffect(() => fetch(), []);

  return (
    <div className="box-border w-min h-min">
      <h2 className="text-lg text-center font-bold">Recommended Users</h2>
      {!fetchError
        ? list.map((elem, index) => {
          console.log(elem);
          return (
            <div key={index}>
              <UserDisplay username={elem.username} />
            </div>
          );
        })
        : "No users to recommend"}
    </div>
  );
}
export default RecommendedUsers;
