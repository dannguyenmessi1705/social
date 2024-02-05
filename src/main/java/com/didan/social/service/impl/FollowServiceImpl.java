package com.didan.social.service.impl;

import com.didan.social.dto.FollowDTO;

import java.util.List;

public interface FollowServiceImpl {
    FollowDTO getFollowers(String userId) throws Exception;
    FollowDTO getFollowings(String userId) throws Exception;
    boolean followUser(String userId) throws Exception;
    boolean unfollowUser(String userId) throws Exception;
}
