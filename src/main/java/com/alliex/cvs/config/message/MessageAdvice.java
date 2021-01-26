package com.alliex.cvs.config.message;

import com.alliex.cvs.service.MessageService;
import com.samskivert.mustache.Mustache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@RequiredArgsConstructor
@ControllerAdvice
@ComponentScan(basePackages = "com.alliex.cvs.config.message")
public class MessageAdvice {

    private final MessageSource messageSource;

    private final MessageService messageService;

    @ModelAttribute("messages")
    public Mustache.Lambda messages(Locale locale, HttpSession session) {
        Locale sessionLocale = messageService.getSessionLocale(session);
        Locale _locale = messageService.getLocale(locale, sessionLocale);

        return (frag, out) -> {
            String body = frag.execute();
            String message = this.messageSource.getMessage(body, null, _locale);
            out.write(message);
        };
    }

}