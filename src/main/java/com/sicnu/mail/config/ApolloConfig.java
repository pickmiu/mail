package com.sicnu.mail.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.Config;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sicnu.mail.pojo.MailTemplate;
import com.sicnu.mail.utils.EmailUtil;
import com.sicnu.mail.utils.JsonUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * apollo 配置读取类
 * 
 * @author Tangliyi (2238192070@qq.com)
 */

@Component
@Data
@Slf4j
public class ApolloConfig {

    @Resource
    private Config config;

    @Resource
    private EmailUtil emailUtil;

    /**
     * 邮件模板
     */
    private Map<String, MailTemplate> mailTemplateMap;

    private Map<String, MailTemplate> getMailTemplateMap() {
        Map<String, MailTemplate> mailTemplateMap = config.getProperty("mail.template", str -> JsonUtils.parse(str,
            new JsonMapper().getTypeFactory().constructParametricType(HashMap.class, String.class, MailTemplate.class)),
            new HashMap(0));
        if (mailTemplateMap.isEmpty()) {
            log.warn("[op:getMailTemplateMap] 配置类读取邮件模板为空");
            emailUtil.sendWarnEmail(
                "com.sicnu.mail.config.ApolloConfig.getMailTemplateMap.([]) line:49 info:配置类读取邮件模板为空 author:pickmiu");
        }
        return mailTemplateMap;
    }

    /**
     * 获取模板内容
     * 
     * @param templateId
     * @return
     */
    public String getTemplate(String templateId) {
        MailTemplate mailTemplate = getMailTemplateMap().get(templateId);
        String templateContent = config.getProperty("mail.template." + mailTemplate.getName(), "");
        if ("".equals(templateContent)) {
            log.warn("[op:getTemplate] 根据模板id获取的模板内容为空");
            emailUtil.sendWarnEmail(
                "com.sicnu.mail.config.ApolloConfig.getTemplate.([java.lang.String]) line:67 info:根据模板id获取的模板内容为空 author:pickmiu");
        }
        return templateContent;
    }

    /**
     * 平台开发者列表
     */
    @Value("#{'${platformDeveloperEmail}'.split(',')}")
    private List<String> developmentEmailList;
}
