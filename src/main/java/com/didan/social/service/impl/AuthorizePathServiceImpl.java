package com.didan.social.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizePathServiceImpl implements com.didan.social.service.AuthorizePathService {
    private final Logger logger = LoggerFactory.getLogger(AuthorizePathServiceImpl.class);
    @Override
    public String getUserIdAuthoried() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.error("Not Authorized");
            throw new Exception("Not Authorized");
        }
        return (String) authentication.getPrincipal();
    }
}
