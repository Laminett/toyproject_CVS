package com.alliex.cvs.service;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageSource messageSource;

    @Value("classpath:messages/message_en.properties")
    private Resource resource;

    public List<Map> getMessages(Locale locale) {
        List<Map> messages = new ArrayList<>();

        InputStream is = null;
        try {
            Properties prop = new Properties();
            is = resource.getInputStream();
            prop.load(is);

            for (Object item : prop.keySet()) {
                String key = (String) item;
                messages.add(ImmutableMap.of(key, messageSource.getMessage(key, null, locale)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    public Locale getLocale(Locale locale, Locale sessionLocale) {
        if (sessionLocale != null) {
            return sessionLocale;
        } else {
            return locale;
        }
    }

    public Locale getSessionLocale(HttpSession session) {
        return (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
    }

}
