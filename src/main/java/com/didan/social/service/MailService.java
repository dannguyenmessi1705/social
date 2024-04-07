package com.didan.social.service;

import java.io.IOException;

public interface MailService {
    void sendTextEmail(String email, String subject, String text) throws IOException;
}
