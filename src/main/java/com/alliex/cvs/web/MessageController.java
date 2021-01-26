package com.alliex.cvs.web;

import com.alliex.cvs.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/messages")
    public List<Map> getMessages(Locale locale, HttpSession session) {
        Locale sessionLocale = messageService.getSessionLocale(session);
        Locale _locale = messageService.getLocale(locale, sessionLocale);

        return messageService.getMessages(_locale);
    }

}
