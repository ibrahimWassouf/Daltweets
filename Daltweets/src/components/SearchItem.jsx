import { Link } from "react-router-dom";

function SearchItem({name,bio,route,isFriend}){
    const desc = route === "profile" ? "bio" : "description"
    function truncateWords(str, maxLength) {
        if (str == undefined) {
            return `No ${desc}`;
        }
        if (str.length <= maxLength) {
            return str;
        }
        return str.slice(0, maxLength) + str.length <= maxLength ? '...' : "";
    }

return (
    <div className="flex items-center mb-2">
        <div>
        <Link 
        to={`/${route}`}
        state={{ username: name, isFriend }}
        className="text-black-500 hover:text-yellow-700"
      > 
        <h3 className="font-bold">{name}</h3>
      </Link>
            <p className="text-gray-500">{truncateWords(bio,50)}</p>
        </div>
    </div>
  )
};
export default SearchItem;
