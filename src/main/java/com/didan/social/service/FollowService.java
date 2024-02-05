package com.didan.social.service;

import com.didan.social.dto.FollowDTO;
import com.didan.social.entity.Followers;
import com.didan.social.entity.Users;
import com.didan.social.entity.keys.FollowerId;
import com.didan.social.repository.FollowRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.FollowServiceImpl;
import com.didan.social.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class FollowService implements FollowServiceImpl {
    private final FollowRepository followRepository;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest request;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(JwtUtils jwtUtils,
                         FollowRepository followRepository,
                         HttpServletRequest request,
                         UserRepository userRepository)
    {
        this.followRepository = followRepository;
        this.jwtUtils = jwtUtils;
        this.request = request;
        this.userRepository = userRepository;
    }

    @Override
    public FollowDTO getFollowers(String userId) throws Exception {
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) throw new Exception("User is not found");
        List<String> userFollows = new ArrayList<>();
        Set<Followers> followers = user.getFolloweds();
        for (Followers follower : followers){
            userFollows.add(follower.getUsers1().getUserId());
        }
        return new FollowDTO(userFollows.size(), userFollows);
    }

    @Override
    public FollowDTO getFollowings(String userId) throws Exception {
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) throw new Exception("User is not found");
        List<String> userFollowings = new ArrayList<>();
        Set<Followers> followings = user.getFollowers();
        for (Followers follower : followings){
            userFollowings.add(follower.getUsers2().getUserId());
        }
        return new FollowDTO(userFollowings.size(), userFollowings);
    }

    @Override
    public boolean followUser(String userId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user.getUserId().equals(userId)) throw new Exception("Can not follow yourself");
        if (userRepository.findFirstByUserId(userId) == null) throw new Exception("The user isnt existed");
        if (user == null) throw new Exception("User is not found");
        Followers follower = new Followers();
        if(followRepository.findFirstByUsers1_UserIdAndUsers2_UserId(user.getUserId(), userId) != null) throw new Exception("This user has already followed");
        else {
            follower.setFolId(new FollowerId(user.getUserId(), userId));
        }
        followRepository.save(follower);
        return true;
    }

    @Override
    public boolean unfollowUser(String userId) throws Exception {
        String accessToken = jwtUtils.getTokenFromHeader(request);
        if (!StringUtils.hasText(accessToken)) throw new Exception("Not Authorized");
        String email = jwtUtils.getEmailUserFromAccessToken(accessToken);
        if (email == null) throw new Exception("Have some errors");
        Users user = userRepository.findFirstByEmail(email);
        if (user == null) throw new Exception("User is not found");
        if (user.getUserId().equals(userId)) throw new Exception("Can not unfollow yourself");
        if (userRepository.findFirstByUserId(userId) == null) throw new Exception("The user isnt existed");
        Followers follower = followRepository.findFirstByUsers1_UserIdAndUsers2_UserId(user.getUserId(), userId);
        if(follower == null) throw new Exception("This user hasn't followed yet");
        else {
            followRepository.delete(follower);
        }
        return true;
    }
}
