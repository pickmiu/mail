package com.sicnu.mail;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sicnu.mail.config.ApolloConfig;
import com.sicnu.mail.pojo.MailTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sicnu.mail.service.MailService;

@SpringBootTest
class MailApplicationTests {

    @Resource
    private MailService mailService;

    @Resource
    private ApolloConfig apolloConfig;

    @Test
    void contextLoads(){
//        sendMail("2238192070@qq.com");
//        System.out.println(apolloConfig.getMailTemplateMap().get("2"));
        System.out.println(apolloConfig.getDevelopmentEmailList().toString());
    }

    void sendMail(String to) {
        Map<String, String> map = new HashMap();
        map.put("msg", "com.sicnu.community.XiaoShiCommunityBasicApplicationTests.contextLoads.([]) line:80 info:测试邮件预警内容 author:pickmiu");
        mailService.sendHTMLEmail("xiaoshi_community@163.com", new String[]{to}, "系统预警", apolloConfig.getTemplate("2"), map);
    }
    @Test
    void json() {
        Map<String, MailTemplate> mailTemplateMap = new HashMap<>();
        mailTemplateMap.put("1", new MailTemplate("验证码模版", "<!DOCTYPE html>\n" +
                "<html lang=\\\"en\\\" xmlns:th=\\\"http://www.thymeleaf.org\\\">\n" +
                "\t<head>\n" +
                "\t    <meta charset=\\\"UTF-8\\\">\n" +
                "\t    <title>yimcarson</title>\n" +
                "\t    <style>\n" +
                "\t        body {\n" +
                "\t            text-align: center;\n" +
                "\t            margin-left: auto;\n" +
                "\t            margin-right: auto;\n" +
                "\t        }\n" +
                "\t        #main {\n" +
                "\t            text-align: center;\n" +
                "\t            position: absolute;\n" +
                "\t        }\n" +
                "\t    </style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div id=\\\"main\\\">\n" +
                "\t    \t<h3>Welcome <span th:text=\\\"${project}\\\"></span> -By <span th:text=\\\" ${author}\\\"></span></h3>\n" +
                "\t    \tYour Verification Code is\n" +
                "\t    \t<h2><span th:text=\\\"${code}\\\"></span></h2>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>"));
        mailTemplateMap.put("2", new MailTemplate("系统报警邮件模板", "无"));
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        System.out.println(gson.toJson(mailTemplateMap));
    }

}
