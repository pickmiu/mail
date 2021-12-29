package com.sicnu.mail.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailTemplate {
    /**
     * 邮件模版描述
     */
    private String desc;
    /**
     * 邮件模板名称
     */
    private String name;
}