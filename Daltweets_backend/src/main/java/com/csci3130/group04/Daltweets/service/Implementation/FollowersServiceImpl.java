package com.csci3130.group04.Daltweets.service.Implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csci3130.group04.Daltweets.model.Followers;
import com.csci3130.group04.Daltweets.model.User;
import com.csci3130.group04.Daltweets.model.Followers.Status;
import com.csci3130.group04.Daltweets.repository.FollowersRepository;
import com.csci3130.group04.Daltweets.service.FollowersService;

@Service
public class FollowersServiceImpl implements FollowersService{
   

    @Autowired
    FollowersRepository followersRepository;
    
    @Override
    public List<User> getAllFollowers(User user) {
       if (user == null || user.getId() < 1) return null;
       return followersRepository.findFollowerIdsByUserId(user.getId());
    }

    @Override
    public Followers addFollower(User user, User follower) {
        if (user == null || follower == null) return null;
        int LatestId = followersRepository.findLatestId() + 1;
        Followers newFollower = new Followers(LatestId,user,follower, Followers.Status.PENDING);
        return followersRepository.save(newFollower);
    }

    @Override
    public Boolean removeFollower(User user, User follower) {
        if (user == null || follower == null) return false;

        Followers foundFollower = followersRepository.findByUserAndFollower(user, follower);
        if (foundFollower != null)
        {
            followersRepository.delete(foundFollower);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getUserFollowing(User follower) {
        if (follower == null || follower.getId() < 1) return null;
        return followersRepository.findUserIdsByFollowerId(follower.getId());
    }    

    @Override
    public Boolean acceptFollowRequest(User user,User follower){
        if (user == null || follower == null) return false;
        Followers follow = followersRepository.findByUserAndFollower(user,follower);
        if (follow != null)
        {
            follow.setStatus(Status.ACCEPTED);
            follow = followersRepository.save(follow) ;
            return follow != null;
        }
        
        return false;
    }

    @Override
    public List<Followers> getFollowRequests(User user) {
       if (user == null) return null;
        return followersRepository.findFollowerRequestsByUserId(user.getId());
    }
}
