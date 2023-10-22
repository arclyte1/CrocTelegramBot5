package ru.croc.croctelegrambottemplate.bot;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

import org.springframework.core.env.Environment;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.croc.croctelegrambottemplate.constants.Constants;
import ru.croc.croctelegrambottemplate.responsehandler.ResponseHandler;
import ru.croc.croctelegrambottemplate.responsehandler.holder.ResponseHandlerHolder;
import ru.croc.croctelegrambottemplate.responsehandler.UnexpectedMessageResponseHandler;
import ru.croc.croctelegrambottemplate.states.UserState;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Основной класс телеграм бота.
 */
public class CrocTelegramBot extends AbilityBot {

    private final Map<UserState, ResponseHandler> responseHandlerMap;

    private final UnexpectedMessageResponseHandler unexpectedMessageResponseHandler;


    /**
     * Конструктор для телеграм - бота.
     * @param environment - окружение Spring Boot.
     *        Из окружения мы сможем вычленить параметры, описанные в application.yml.
     */
    public CrocTelegramBot(Environment environment) {
        super(environment.getProperty("bot.token"), environment.getProperty("bot.name"));
        var responseHandlerHolder = new ResponseHandlerHolder(silent, db());
        responseHandlerMap = responseHandlerHolder.getResponseHandlerMap();
        unexpectedMessageResponseHandler = new UnexpectedMessageResponseHandler(silent, db());
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    /**
     * Возможность старта бота.
     *
     * @return Ability класс, описывающий возможности бота.
     */
    public Ability startBot() {
        return Ability.builder()
            .name("start") // У пользователя появится возможность /start бота и на это действие навешен обработчик.
            .info(Constants.START_DESCRIPTION)
            .locality(Locality.USER)
            .privacy(Privacy.PUBLIC)
            .action(ctx -> responseHandlerMap.get(UserState.START_BOT).handle(ctx.chatId(), null)) // Обработчик команды старта бота.
            .build();
    }

    /**
     * Основной метод, обрабатывающий действия от нажатия кнопок пользователем.
     * @return ответное действие.
     */
    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (baseAbilityBot, update) -> {
            Long chatId = getChatId(update);
            UserState userState = db().<Long, UserState>getMap(Constants.CHAT_STATES).get(chatId);
            responseHandlerMap.getOrDefault(
                userState,
                unexpectedMessageResponseHandler
            ).handle(chatId, update.getMessage());
        };
        Predicate<Update> predicate = update -> {
            Long chatId = getChatId(update);
            UserState userState = db().<Long, UserState>getMap(Constants.CHAT_STATES).get(chatId);
            return responseHandlerMap.getOrDefault(userState, unexpectedMessageResponseHandler).userIsActive(chatId);
        };
        return Reply.of(action, Flag.TEXT, predicate);
    }
}
