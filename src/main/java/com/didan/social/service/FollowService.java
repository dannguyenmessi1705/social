package com.didan.social.service;

import com.didan.social.dto.FollowDTO;
import com.didan.social.entity.Followers;
import com.didan.social.entity.Users;
import com.didan.social.entity.keys.FollowerId;
import com.didan.social.repository.FollowRepository;
import com.didan.social.repository.UserRepository;
import com.didan.social.service.impl.AuthorizePathServiceImpl;
import com.didan.social.service.impl.FollowServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService implements FollowServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(FollowService.class);
    private final FollowRepository followRepository;
    private final AuthorizePathServiceImpl authorizePathService;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository,
                         AuthorizePathServiceImpl authorizePathService,
                         UserRepository userRepository)
    {
        this.followRepository = followRepository;
        this.authorizePathService = authorizePathService;
        this.userRepository = userRepository;
    }

    @Override
    public FollowDTO getFollowers(String userId) throws Exception {
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
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
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        List<String> userFollowings = new ArrayList<>();
        Set<Followers> followings = user.getFollowers();
        for (Followers follower : followings){
            userFollowings.add(follower.getUsers2().getUserId());
        }
        return new FollowDTO(userFollowings.size(), userFollowings);
    }

    @Override
    public boolean followUser(String userIdFollow) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        if (user.getUserId().equals(userIdFollow)) {
            logger.warn("Can not follow yourself");
            throw new Exception("Can not follow yourself");
        }
        if (userRepository.findFirstByUserId(userIdFollow) == null) {
            logger.error("The user isnt existed");
            throw new Exception("The user isnt existed");
        }
        Followers follower = new Followers();
        if(followRepository.findFirstByUsers1_UserIdAndUsers2_UserId(user.getUserId(), userIdFollow) != null) {
            logger.error("This user has already followed");
            throw new Exception("This user has already followed");
        }
        else {
            follower.setFolId(new FollowerId(user.getUserId(), userIdFollow));
        }
        followRepository.save(follower);
        return true;
    }

    @Override
    public boolean unfollowUser(String userIdUnfollow) throws Exception {
        String userId = authorizePathService.getUserIdAuthoried();
        Users user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            logger.error("User is not found");
            throw new Exception("User is not found");
        }
        if (user.getUserId().equals(userIdUnfollow)) {
            logger.warn("Can not unfollow yourself");
            throw new Exception("Can not unfollow yourself");
        }
        if (userRepository.findFirstByUserId(userIdUnfollow) == null) {
            logger.error("The user isnt existed");
            throw new Exception("The user isnt existed");
        }
        Followers follower = followRepository.findFirstByUsers1_UserIdAndUsers2_UserId(user.getUserId(), userIdUnfollow);
        if(follower == null) {
            logger.error("This user hasn't followed yet");
            throw new Exception("This user hasn't followed yet");
        }
        else {
            followRepository.delete(follower);
        }
        return true;
    }
}
