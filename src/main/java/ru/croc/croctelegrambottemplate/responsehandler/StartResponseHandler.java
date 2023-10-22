package ru.croc.croctelegrambottemplate.responsehandler;

import static ru.croc.croctelegrambottemplate.constants.Constants.START_TEXT;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.croc.croctelegrambottemplate.states.UserState;

public class StartResponseHandler extends ResponseHandler {

    public StartResponseHandler(SilentSender sender, DBContext db) {
        super(sender, db);
    }

    /**
     * Обработчик ответа на команду "start".
     * @param chatId идентификатор чата с пользователем.
     */
    private void replayToStart(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(START_TEXT);
        sender.execute(sendMessage);
        chatStates.put(chatId, UserState.AWAITING);
    }

    @Override
    public void handleMessage(long chatId, Message message) {
        replayToStart(chatId);
    }

    @Override
    public UserState getUserState() {
        return UserState.START_BOT;
    }
}
