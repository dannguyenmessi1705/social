package com.didan.social.service;

import com.didan.social.service.impl.MailServiceImpl;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService implements MailServiceImpl {
    private final Environment env;
    @Autowired
    public MailService(Environment env){
        this.env = env;
    }
    @Override
    public void sendTextEmail(String email, String subject, String text) throws IOException {
        Email from = new Email();
        from.setEmail(env.getProperty("send_grid.from_email"));
        from.setName(env.getProperty("send_grid.from_name"));
        Email to = new Email(email);
        Content content = new Content("text/html", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(env.getProperty("send_grid.api_key"));
        Request request = new Request();
        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        }catch (IOException e){
            throw new IOException("Server Error", e);
        }
    }
}
