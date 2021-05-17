package com.milad.mehdirad.support.radbot.controller;

import com.milad.mehdirad.support.radbot.config.BaseConfig;
import com.milad.mehdirad.support.radbot.listener.SendListener;
import com.milad.mehdirad.support.radbot.listener.UpdateListener;
import com.milad.mehdirad.support.radbot.repository.UserRepository;
import com.milad.mehdirad.support.radbot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Controller
public class BotController implements UpdateListener {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BotService botService;

    @Autowired
    SendListener sendListener;

    @Override
    public void respondedListener(Update update) {

        long fromUserId = update.getMessage().getFrom().getId();


        boolean isPendingUser =
                botService.pendingPhonePermission.stream().anyMatch(a -> a == fromUserId);

        if (isPendingUser && update.getMessage().getContact() != null && !update.getMessage().getContact().getPhoneNumber().isEmpty()) {

            userRepository.save(getUserFromUpdate(update));
            botService.pendingPhonePermission.remove(fromUserId);

            SendMessage message = botService.createMessage(BaseConfig.registredMSG, String.valueOf(fromUserId));
            sendListener.send(message);
        }

        if (botService.respondedIsAdmin(update)) {
            String textMessage = update.getMessage().getReplyToMessage().getText();
            long replyToUserId = Long.parseLong(textMessage);

            if (update.getMessage().getText().equals(BaseConfig.commandBlockUser)) {
                com.milad.mehdirad.support.radbot.model.User user = userRepository.findUserByUserId(replyToUserId);
                user.setBlocked(true);
                userRepository.save(user);
                SendMessage blockedUserMSG = botService.createMessage(BaseConfig.userBloked, String.valueOf(update.getMessage().getFrom().getId()));
                sendListener.send(blockedUserMSG);
            } else {
                SendMessage adminResponseMSG = botService.createMessage(update.getMessage().getText(), String.valueOf(replyToUserId));
                sendListener.send(adminResponseMSG);
                SendMessage showResultForAdmin = botService.createMessage(BaseConfig.MSGSended, String.valueOf(update.getMessage().getFrom().getId()));
                sendListener.send(showResultForAdmin);
            }
        }

    }

    @Override
    public void updateListener(Update update) {

        botService.verifyUser(update);

    }

    public com.milad.mehdirad.support.radbot.model.User getUserFromUpdate(Update update) {

        User from = update.getMessage().getFrom();
        Long userId = from.getId();
        String phone = update.getMessage().getContact().getPhoneNumber();
        String username = from.getUserName();
        String name = from.getFirstName() + " " + from.getLastName();

        return new com.milad.mehdirad.support.radbot.model.User(userId, phone, username, name);

    }


}
