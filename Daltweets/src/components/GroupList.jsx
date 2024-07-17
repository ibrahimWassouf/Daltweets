import axios from "axios";
import { useEffect, useState } from "react";

export default function GroupList() {
  const [groups, setGroups] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_BACKEND_BASE_URL}/api/group/all-groups`
        );

        setGroups(response.data);
      } catch (error) {
        console.error("Error get data", error);
      }
    };
    fetchData();
  }, []);

  let list = groups.map((elem, idx) => (
    <li key={idx} className="mx-auto w-full flex justify-between p-2">
      <p className="w-1/3">{elem.name}</p>
    </li>
  ));
  return (
    <>
      <div className=" box-border mx-auto w-3/6 flex justify-center">
        <ul className="w-full flex flex-col justify-center ">{list}</ul>
      </div>
    </>
  );
}
