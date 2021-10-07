package com.milad.mehdirad.support.radbot.controller;

import com.milad.mehdirad.support.radbot.listener.SendListener;
import com.milad.mehdirad.support.radbot.listener.UpdateListener;
import com.milad.mehdirad.support.radbot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
public class BotController implements UpdateListener {



    @Autowired
    BotService botService;

    @Autowired
    SendListener sendListener;

    @Override
    public void respondedListener(Update update) {


    }

    @Override
    public void updateListener(Update update) {

        botService.verifyUser(update);

    }




}
