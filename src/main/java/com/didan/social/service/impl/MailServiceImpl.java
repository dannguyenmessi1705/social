package com.didan.social.service.impl;

import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public interface MailServiceImpl {
    void sendTextEmail(String email, String subject, String text) throws IOException;
}
