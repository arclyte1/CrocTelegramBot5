package ru.croc.croctelegrambottemplate.responsehandler;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.croc.croctelegrambottemplate.constants.Constants;
import ru.croc.croctelegrambottemplate.keyboard.KeyboardFactory;
import ru.croc.croctelegrambottemplate.states.UserState;

import java.text.MessageFormat;

/**
 * Обработчик ответа от пользователя после ввода имени.
 */
public class ReplyToChooseCalculationHandler extends ResponseHandler {

    public ReplyToChooseCalculationHandler(SilentSender sender, DBContext db) {
        super(sender, db);
    }

    @Override
    protected void handleMessage(long chatId, Message message) {
        replyToChooseCalculation(chatId, message);
    }

    private void replyToChooseCalculation(long chatId, Message message) {
        if (!message.getText().equalsIgnoreCase(Constants.INVEST_CALCULATION_NAME)) {
            unexpectedMessage(chatId, KeyboardFactory.getReplyToName());
            return;
        }
        String messageToReplay = MessageFormat.format(
            "Окей, я тебя понял, {0}. Начинаем расчет...",
            MAP_CHATID_USERNAME.getOrDefault(chatId, "username")
        );
        promptWithKeyboardForState(
            chatId,
            messageToReplay,
            null,
            UserState.INVEST_CALCULATION
        );
    }

    @Override
    public UserState getUserState() {
        return UserState.AWAITING_CHOICE_CALCULATION;
    }
}
