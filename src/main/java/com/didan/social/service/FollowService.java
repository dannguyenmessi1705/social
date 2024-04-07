package com.didan.social.service;

import com.didan.social.dto.FollowDTO;

public interface FollowService {
    FollowDTO getFollowers(String userId) throws Exception;
    FollowDTO getFollowings(String userId) throws Exception;
    boolean followUser(String userId) throws Exception;
    boolean unfollowUser(String userId) throws Exception;
}
