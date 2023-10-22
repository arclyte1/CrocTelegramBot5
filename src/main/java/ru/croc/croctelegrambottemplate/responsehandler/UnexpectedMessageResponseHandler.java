package ru.croc.croctelegrambottemplate.responsehandler;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.croc.croctelegrambottemplate.states.UserState;

/**
 * Обраобтчик всех неизвестных статусов текущего пользователя.
 * Если какой-то обработчик не найден по UserState, то вызывается данный обработчик.
 */
public class UnexpectedMessageResponseHandler extends ResponseHandler {

    public UnexpectedMessageResponseHandler(SilentSender sender, DBContext db) {
        super(sender, db);
    }

    @Override
    protected void handleMessage(long chatId, Message message) {
        unexpectedMessage(chatId);
    }

    /**
     * Метод обработки неопознанных сообщений.
     * @param chatId идентификатор сообщения с пользователем.
     */
    protected void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Я не знаю что это за действие :(");
        sender.execute(sendMessage);
    }

    @Override
    public UserState getUserState() {
        return null;
    }
}
