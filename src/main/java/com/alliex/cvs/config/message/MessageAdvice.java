package com.alliex.cvs.config.message;

import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice
@ComponentScan(basePackages = "com.alliex.cvs.config.message")
public class MessageAdvice {

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("messages")
    public Mustache.Lambda messages(Locale locale){
        return (frag, out) -> {
          String body = frag.execute();
          String message = this.messageSource.getMessage(body, null, locale);
          out.write(message);
        };
    }
}
