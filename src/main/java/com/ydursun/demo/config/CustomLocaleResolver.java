package com.ydursun.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class CustomLocaleResolver
        extends AcceptHeaderLocaleResolver
        implements WebMvcConfigurer {

    @Value("${spring.messages.basename}")
    private String messagesBaseName;

    @Value("${spring.messages.encoding}")
    private String messagesEncoding;

    @Value("${spring.messages.fallback-to-system-locale}")
    private boolean messagesFallbackToSystemLocale;

    private final List<Locale> locales = Arrays.asList(
            new Locale("en"),
            new Locale("tr"));

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), locales);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasenames(messagesBaseName.split(","));
        rs.setDefaultEncoding(messagesEncoding);
        rs.setUseCodeAsDefaultMessage(true);
        rs.setFallbackToSystemLocale(messagesFallbackToSystemLocale);
        return rs;
    }

}
