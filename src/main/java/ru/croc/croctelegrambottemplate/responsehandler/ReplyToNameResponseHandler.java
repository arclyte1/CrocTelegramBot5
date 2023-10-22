package ru.croc.croctelegrambottemplate.responsehandler;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.croc.croctelegrambottemplate.keyboard.KeyboardFactory;
import ru.croc.croctelegrambottemplate.states.UserState;

import java.text.MessageFormat;

/**
 * Обработчик, который действует при вводе имени от пользователя.
 */
public class ReplyToNameResponseHandler extends ResponseHandler {

    public ReplyToNameResponseHandler(SilentSender sender, DBContext db) {
        super(sender, db);
    }

    @Override
    public void handleMessage(long chatId, Message message) {
        replyToName(chatId, message);
    }

    /**
     * Ответ пользователю после того, как он ввёл свое имя.
     * @param chatId идентфикатор чата с пользователем.
     * @param message введенное пользователем сообщение.
     */
    private void replyToName(long chatId, Message message) {
        String userName = message.getText();
        String messageToReplay = MessageFormat.format(
            "Привет, {0}. Что бы ты хотел выбрать в данном случае?",
            userName
        );
        MAP_CHATID_USERNAME.putIfAbsent(chatId, userName);
        promptWithKeyboardForState(
            chatId,
            messageToReplay,
            KeyboardFactory.getReplyToName(),
            UserState.AWAITING_CHOICE_CALCULATION
        );
    }

    @Override
    public UserState getUserState() {
        return UserState.AWAITING;
    }
}
