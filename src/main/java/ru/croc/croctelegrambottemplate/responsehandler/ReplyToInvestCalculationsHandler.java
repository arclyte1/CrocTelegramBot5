package ru.croc.croctelegrambottemplate.responsehandler;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.croc.croctelegrambottemplate.keyboard.KeyboardFactory;
import ru.croc.croctelegrambottemplate.states.UserState;

import java.text.MessageFormat;
import java.util.Arrays;

public class ReplyToInvestCalculationsHandler extends ResponseHandler {

    public ReplyToInvestCalculationsHandler(SilentSender sender, DBContext db) {
        super(sender, db);
    }

    private void makeCalculations(long chatId, Message message) {
        String params = message.getText();
        String messageToReplay = "";
        UserState newState;
        try {
            var doubles = Arrays.stream(params.split(" ")).map(Double::parseDouble).toList();

            if (!doubles.stream().allMatch((it) -> { return it >= 0; })) {
                messageToReplay = "Все параметры должны быть положительные";
                newState = UserState.INVEST_CALCULATION;
            } else {
                messageToReplay = MessageFormat.format(
                        "Результат: {0}",
                        doubles.get(0) * Math.pow(1 + doubles.get(2) / 100., doubles.get(1))
                );
                newState = UserState.AWAITING_CHOICE_CALCULATION;
            }
        } catch (Exception e) {
            messageToReplay = "У меня не получилось распарсить твои параметры";
            newState = UserState.INVEST_CALCULATION;
        }

        promptWithKeyboardForState(
            chatId,
            messageToReplay,
            KeyboardFactory.getReplyToName(),
            newState
        );
    }

    @Override
    protected void handleMessage(long chatId, Message message) {
        makeCalculations(chatId, message);
    }

    @Override
    public UserState getUserState() {
        return UserState.INVEST_CALCULATION;
    }
}
