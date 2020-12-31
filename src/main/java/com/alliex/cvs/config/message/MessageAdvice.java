package com.alliex.cvs.config.message;

import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
@ComponentScan(basePackages = "com.alliex.cvs.config.message")
public class MessageAdvice {

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("messages")
    public Mustache.Lambda messages(Locale locale, HttpServletRequest request) {
        Object lang = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        Locale finalLocale = lang == null ? locale : (Locale) lang;
        return (frag, out) -> {
            String body = frag.execute();
            String message = this.messageSource.getMessage(body, null, finalLocale);
            out.write(message);
        };
    }
}