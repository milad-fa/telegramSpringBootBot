package com.milad.mehdirad.support.radbot.config;

import com.milad.mehdirad.support.radbot.listener.SendListener;
import com.milad.mehdirad.support.radbot.listener.UpdateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BaseBot extends TelegramLongPollingBot implements SendListener {

    @Autowired
    UpdateListener updateListener;

    @Override
    public String getBotUsername() {
        return BaseConfig.botUsername;
    }

    @Override
    public String getBotToken() {
        return BaseConfig.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().isReply()) {
            updateListener.respondedListener(update);
        } else {
            updateListener.updateListener(update);
        }
    }

    @Override
    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(ForwardMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
