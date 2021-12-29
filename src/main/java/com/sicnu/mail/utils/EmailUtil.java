package com.sicnu.mail.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sicnu.mail.config.ApolloConfig;
import com.sicnu.mail.service.MailService;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Component
public class EmailUtil {

    @Resource
    private ApolloConfig apolloConfig;

    @Resource
    private MailService mailService;

    public void sendWarnEmail(String msg) {
        Map<String, String> params = new HashMap<>(1);
        params.put("msg", msg);
        List<String> developmentEmailList = apolloConfig.getDevelopmentEmailList();
        mailService.sendHTMLEmail(developmentEmailList.toArray(new String[developmentEmailList.size()]), "系统预警", "2",
            params);
    }
}
