package com.sicnu.mail.service;

import java.util.Map;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
public interface MailService {

    int sendHTMLEmail(String from, String[] to, String subject, String template, Map<String, String> params);

    int sendHTMLEmail(String[] to, String subject, String templateId, Map<String, String> params);
}
