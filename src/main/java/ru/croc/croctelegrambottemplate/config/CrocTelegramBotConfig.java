package ru.croc.croctelegrambottemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.croc.croctelegrambottemplate.bot.CrocTelegramBot;

/**
 * Конфигурация для использования бота.
 * По умолчанию, бот не работает с Spring Boot >= 3.0.
 * В данном случае создается бин TelegramBotsApi.
 */
@Configuration
public class CrocTelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class);
    }

    /**
     * Регистрация нашего бота в TelegramBotsApi.
     * @param telegramBotsApi - апи для бота в Телеграм.
     * @param environment окружение приложения SpringBoot.
     * @return созданный бин нашего телеграм бота.
     * @throws TelegramApiException исключение при некорректной работе Telegram API.
     */
    @Bean
    public CrocTelegramBot crocTelegramBot(
        TelegramBotsApi telegramBotsApi,
        Environment environment
    ) throws TelegramApiException {
        CrocTelegramBot crocTelegramBot = new CrocTelegramBot(environment);
        telegramBotsApi.registerBot(crocTelegramBot);
        return crocTelegramBot;
    }
}
