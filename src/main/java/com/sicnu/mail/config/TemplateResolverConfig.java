package com.sicnu.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.StringTemplateResolver;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Configuration
public class TemplateResolverConfig {

    /**
     * 配置thymeleaf模板引擎的解析器
     * 
     * @return
     */
    @Bean
    public StringTemplateResolver defaultTemplateResolver() {
        return new StringTemplateResolver();
    }
}
