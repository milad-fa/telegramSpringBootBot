package com.milad.mehdirad.support.radbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BaseConfig {

    public static String botUsername = "";
    public static String botToken = "";
    public static long[] admins = {119144033};

    public static String textRequestPhone = "جهت ارتباط با کانال دکمه ی ارسال شماره را فشار دهید .";
    public static String sharePhoneTitle = "اشتراک شماره تلفن";
    public static String registredMSG = "عضویت شما در سیستم با موفقیت انجام شدم.لطفا پیام خود را در قالب یک متن ارسال فرمایید.";
    public static String recivedMessage = "پیام شما با موفقیت دریافت شد.";
    public static String commandBlockUser = "/b";
    public static String userBloked = "کاربر بلاک شد.";
    public static String MSGSended = "پیام شما به کاربر تحویل داده شد.\uD83C\uDF39";
    public static String youInBlockList = "شما در لیست بلاک هستید.";
    public static String commandSendForAllUser = "/a";


    @Bean
    public TelegramBotsApi getTelegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(getBot());
        return telegramBotsApi;
    }

    @Bean
    public BaseBot getBot() {
        return new BaseBot();
    }

}
