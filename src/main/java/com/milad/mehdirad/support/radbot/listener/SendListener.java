package com.milad.mehdirad.support.radbot.listener;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public interface SendListener {
    void send(SendMessage message);

    void send(ForwardMessage message);

}
