package com.sicnu.mail.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage implements Serializable {
    private String[] to;
    private String templateId;
    private String subject;
    private Map<String, String> params;
}