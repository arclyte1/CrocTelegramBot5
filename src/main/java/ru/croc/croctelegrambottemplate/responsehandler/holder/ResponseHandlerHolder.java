package ru.croc.croctelegrambottemplate.responsehandler.holder;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.croc.croctelegrambottemplate.responsehandler.*;
import ru.croc.croctelegrambottemplate.states.UserState;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Держатель обработчиков ответов пользователя для телеграм.
 */
public class ResponseHandlerHolder {

    /**
     * Мапа "Состояние пользователя" - "Обработчик ответов"
     */
    private final Map<UserState, ResponseHandler> responseHandlerMap;

    /**
     * Конструктор держателя обработчиков ответов.
     * @param sender - отправщик сообщений пользователю в Телеграм
     * @param dbContext - контекст хранения информации.
     */
    public ResponseHandlerHolder(SilentSender sender, DBContext dbContext) {
        responseHandlerMap = Stream.of(
            new StartResponseHandler(sender, dbContext),
            new ReplyToNameResponseHandler(sender, dbContext),
            new ReplyToChooseCalculationHandler(sender, dbContext),
            new ReplyToInvestCalculationsHandler(sender, dbContext)
        )
            .collect(Collectors.toMap(ResponseHandler::getUserState, Function.identity()));
    }

    /**
     * Геттер для получения обработчиков ответов.
     * @return обработчики ответов.
     */
    public Map<UserState, ResponseHandler> getResponseHandlerMap() {
        return responseHandlerMap;
    }
}
