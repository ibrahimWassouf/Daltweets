import FriendRequest from "../components/FollowRequest";

export default function FollowRequestTab({ requests, onAccept, onRemove }) {
  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <div className="flex flex-col w-full items-center friend">
      {requests.length > 0 ? (
        requests.map((request, i) => {
          return (
            <FriendRequest
              onAccept={onAccept}
              onRemove={onRemove}
              key={i}
              username={user.username}
              followerName={request.follower.username}
            />
          );
        })
      ) : (
        <p className="m-10">No follow Requests....</p>
      )}
    </div>
  );
}
