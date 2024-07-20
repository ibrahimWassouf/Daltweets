import Friend from "../components/Friend";

export default function FollowersTab({ followers, onRemove }) {
  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <>
      <div className="flex flex-col w-full items-center friend">
        {followers.length > 0 ? (
          followers.map((follower, i) => {
            return (
              <Friend
                onRemove={onRemove}
                key={i}
                username={user.username}
                followerName={follower.username}
              />
            );
          })
        ) : (
          <p className="m-10">No Followers....</p>
        )}
      </div>
    </>
  );
}
