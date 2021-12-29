package com.sicnu.mail.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.sicnu.mail.pojo.SpecificMimeMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义JavaMailSender
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
public class SpecificJavaMailSenderImpl extends JavaMailSenderImpl {

    private static final String HEADER_MESSAGE_ID = "Message-ID";

    private volatile Transport transport;

    private String appName;

    public void initTransport() throws MessagingException {
        long start = System.currentTimeMillis();
        this.transport = connectTransport();
        log.info("[op:initTransport] connect mail server: {} takes: {}ms", transport.isConnected(),
            System.currentTimeMillis() - start);
    }

    public SpecificJavaMailSenderImpl(String appName) {
        this.appName = appName;
    }

    @Override
    protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
        Map<Object, Exception> failedMessages = new LinkedHashMap<>();

        for (int i = 0; i < mimeMessages.length; i++) {
            // Check transport connection first...
            if (transport == null || !transport.isConnected()) {
                if (transport != null) {
                    try {
                        transport.close();
                    } catch (Exception ex) {
                        // Ignore - we're reconnecting anyway
                    }
                    transport = null;
                }
                try {
                    transport = connectTransport();
                } catch (AuthenticationFailedException ex) {
                    throw new MailAuthenticationException(ex);
                } catch (Exception ex) {
                    // Effectively, all remaining messages failed...
                    for (int j = i; j < mimeMessages.length; j++) {
                        Object original = (originalMessages != null ? originalMessages[j] : mimeMessages[j]);
                        failedMessages.put(original, ex);
                    }
                    throw new MailSendException("Mail server connection failed", ex, failedMessages);
                }
            }

            // Send message via current transport...
            MimeMessage mimeMessage = mimeMessages[i];
            try {
                if (mimeMessage.getSentDate() == null) {
                    mimeMessage.setSentDate(new Date());
                }
                String messageId = mimeMessage.getMessageID();
                mimeMessage.saveChanges();
                if (messageId != null) {
                    // Preserve explicitly specified message id...
                    mimeMessage.setHeader(HEADER_MESSAGE_ID, messageId);
                }
                Address[] addresses = mimeMessage.getAllRecipients();
                transport.sendMessage(mimeMessage, (addresses != null ? addresses : new Address[0]));
            } catch (Exception ex) {
                Object original = (originalMessages != null ? originalMessages[i] : mimeMessage);
                failedMessages.put(original, ex);
            }
        }

        if (!failedMessages.isEmpty()) {
            throw new MailSendException(failedMessages);
        }
    }

    @Override
    public MimeMessage createMimeMessage() {
        return new SpecificMimeMessage(getSession(), getDefaultEncoding(), getDefaultFileTypeMap(), appName);
    }
}
