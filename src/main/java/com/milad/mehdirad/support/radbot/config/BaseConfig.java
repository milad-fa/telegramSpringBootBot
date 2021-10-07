package com.milad.mehdirad.support.radbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BaseConfig {

    public static String botUsername = "@";
    public static String botToken = "";
    public static long[] admins = {119144033};



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
