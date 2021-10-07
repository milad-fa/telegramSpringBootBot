package com.milad.mehdirad.support.radbot.service;

import com.milad.mehdirad.support.radbot.listener.SendListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotService {

    //id list pending phone numbers
    public List<Long> pendingPhonePermission = new ArrayList<>();


    @Autowired
    SendListener sendListener;
    private long fromUserId;

    public void verifyUser(Update update) {
        fromUserId = update.getMessage().getFrom().getId();


    }


}