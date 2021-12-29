package com.sicnu.mail.config;

import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.sicnu.mail.service.impl.SpecificJavaMailSenderImpl;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Configuration
@AutoConfigureAfter(MailProperties.class)
public class MailConfig {

    /**
     * 用于生成唯一id的应用名称 多实例时需保证应用名称不同
     */
    @Value("${app.name}")
    private String appName;

    /**
     * 配置mailSender的属性
     * 
     * @param properties
     * @return
     * @throws MessagingException
     */
    @Bean
    SpecificJavaMailSenderImpl mailSender(MailProperties properties) throws MessagingException {
        SpecificJavaMailSenderImpl sender = new SpecificJavaMailSenderImpl(appName);
        applyProperties(properties, sender);
        sender.initTransport();
        return sender;
    }

    @Bean
    public Config config() {
        return ConfigService.getAppConfig();
    }

    private void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

}
