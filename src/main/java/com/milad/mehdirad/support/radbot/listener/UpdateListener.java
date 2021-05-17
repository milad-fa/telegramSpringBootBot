package com.milad.mehdirad.support.radbot.listener;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public interface UpdateListener {

    void respondedListener(Update update);

    void updateListener(Update update);

}
