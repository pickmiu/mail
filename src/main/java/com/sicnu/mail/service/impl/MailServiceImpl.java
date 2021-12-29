package com.sicnu.mail.service.impl;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sicnu.mail.config.ApolloConfig;
import com.sicnu.mail.service.MailService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    @Resource
    private ApolloConfig apolloConfig;

    @Override
    public int sendHTMLEmail(String from, String[] to, String subject, String template, Map<String, String> params) {
        long start = System.currentTimeMillis();
        Context context = new Context();
        Set<String> keySet = params.keySet();
        for (String key: keySet) {
            context.setVariable(key, params.get(key));
        }
        String emailContent = templateEngine.process(template, context);
        log.info("[op:sendHTMLEmail] 渲染html邮件耗时: {}ms", System.currentTimeMillis() - start);
        System.out.println(emailContent);
        start = System.currentTimeMillis();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            javaMailSender.send(message);
            log.info("[op:sendHTMLEmail] send mail takes: {}ms, to: {}", System.currentTimeMillis() - start, to);
            return 1;
        } catch (MessagingException e) {
            log.error("[op:sendHTMLEmail] catch-exception template={} params={}", template, params, e);
            return 0;
        }
    }

    public int sendHTMLEmail(String[] to, String subject, String templateId, Map<String, String> map) {
        return sendHTMLEmail(fromEmailAddress, to, subject, apolloConfig.getTemplate(templateId), map);
    }

}
