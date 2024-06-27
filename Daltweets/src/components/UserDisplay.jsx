function UserDisplay({ username }) {
  return (
    <div className="box-border w-min m-auto flex flex-column p-1">
      <div className=" border border-black rounded-2xl m-auto border-gray-500 w-64 p-5">
        <div className="flex flex-row w-32">
          <div className="box-border h-min w-min p-2">
            <p className="text-black font-medium m-auto">{username}</p>
          </div>
          <div className="box-border h32 w-24 ml-24">
            <button
              type="submit"
              className="flex w-full justify-center rounded-md bg-yellow-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              <p className="text-bold text-grey-700">Follow</p>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default UserDisplay;
