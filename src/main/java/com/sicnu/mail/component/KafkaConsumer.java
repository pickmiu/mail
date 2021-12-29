package com.sicnu.mail.component;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sicnu.mail.pojo.MailMessage;
import com.sicnu.mail.service.MailService;
import com.sicnu.mail.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
@Slf4j
public class KafkaConsumer {
    @Resource
    private MailService mailService;

    @KafkaListener(topics = "mail", groupId = "mail1")
    public void onMessage1(ConsumerRecord<?, String> record, Consumer consumer) {
        try {
            MailMessage mailMessage = JsonUtils.parse(record.value(), MailMessage.class);
            mailService.sendHTMLEmail(mailMessage.getTo(), mailMessage.getSubject(), mailMessage.getTemplateId(),
                mailMessage.getParams());
            // 异步提交offset
            consumer.commitAsync();
            log.info("[op:onMessage1] consuming success offset={} {}", record.offset(), new Date(record.timestamp()));
        } catch (Exception e) {
            log.error("[op:onMessage1] catch-exception consuming fail record={} consumer={}", record, consumer, e);
        }
    }
}
