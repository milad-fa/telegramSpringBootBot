package com.milad.mehdirad.support.radbot.service;

import com.milad.mehdirad.support.radbot.config.BaseConfig;
import com.milad.mehdirad.support.radbot.listener.SendListener;
import com.milad.mehdirad.support.radbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BotService {

    //id list pending phone numbers
    public List<Long> pendingPhonePermission = new ArrayList<>();

    @Autowired
    UserRepository userRepository;
    @Autowired
    SendListener sendListener;
    private long fromUserId;

    public void verifyUser(Update update) {
        fromUserId = update.getMessage().getFrom().getId();

        if (isAdmin(fromUserId) && update.getMessage().getText().startsWith(BaseConfig.commandSendForAllUser)) {
            sendMessageForAllUsers(update.getMessage().getText());
        }

        if (isExitsUserInBD(fromUserId)) {
            //send and forward message user to admin list
            if (!isAdmin(fromUserId)) {
                if (isBlocked(fromUserId)) {
                    SendMessage sendMessageYouIsBlock = createMessage(BaseConfig.youInBlockList, String.valueOf(fromUserId));
                    sendListener.send(sendMessageYouIsBlock);
                    return;
                }
                sendReceivedMSGForAdmins(update);
                SendMessage successfulMessage = createMessage(BaseConfig.recivedMessage, String.valueOf(fromUserId));
                sendListener.send(successfulMessage);
            }


        } else {
            pendingPhonePermission.add(fromUserId);
            requestPhoneNumber();
        }

    }

    public void sendMessageForAllUsers(String text) {
        List<String> allUserList = getAllUserIds();
        String finalText = text.substring(BaseConfig.commandSendForAllUser.length());
        for (String userId : allUserList) {
            SendMessage message = createMessage(finalText, userId);
            sendListener.send(message);
        }
    }

    public List<String> getAllUserIds() {
        List<String> allUserIdList = new ArrayList<>();
        userRepository.findAll().stream().forEach(a -> {
            allUserIdList.add(String.valueOf(a.getUserId()));
        });
        return allUserIdList;
    }

    private void sendReceivedMSGForAdmins(Update update) {

        for (Long id : BaseConfig.admins) {
            ForwardMessage message = new ForwardMessage();
            message.setMessageId(update.getMessage().getMessageId());
            message.setFromChatId(String.valueOf(update.getMessage().getFrom().getId()));
            message.setChatId(String.valueOf(id));

            //can not get user id from forwarded message
            SendMessage userId = createMessage(String.valueOf(update.getMessage().getFrom().getId()), String.valueOf(id));
            sendListener.send(message);
            sendListener.send(userId);
        }


    }

    public boolean isExitsUserInBD(Long userId) {
        return userRepository.existsByUserId(userId);
    }


    public void requestPhoneNumber() {

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestContact(true);
        keyboardButton.setText(BaseConfig.sharePhoneTitle);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(keyboardButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(fromUserId));
        message.setText(BaseConfig.textRequestPhone);
        message.setReplyMarkup(replyKeyboardMarkup);

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);

        sendListener.send(message);
    }

    public SendMessage createMessage(String text, String chatId) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);

        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        message.setReplyMarkup(replyKeyboardRemove);
        return message;
    }

    public boolean isAdmin(long userId) {
        return Arrays.stream(BaseConfig.admins).anyMatch(a -> a == userId);
    }

    public boolean respondedIsAdmin(Update update) {
        User respondentUser = update.getMessage().getFrom();
        return isAdmin(respondentUser.getId());
    }

    public boolean isBlocked(long userId) {
        return userRepository.findUserByUserId(userId).isBlocked();
    }
}
