import Friend from "../components/Friend";

export default function FollowingTab({ followings, onRemove }) {
  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <>
      <div className="flex flex-col w-full items-center friend">
        {followings.length > 0 ? (
          followings.map((following, i) => {
            return (
              <Friend
                onRemove={onRemove}
                key={i}
                username={following.username}
                followerName={user.username}
              />
            );
          })
        ) : (
          <p className="m-10">No Following....</p>
        )}
      </div>
    </>
  );
}
