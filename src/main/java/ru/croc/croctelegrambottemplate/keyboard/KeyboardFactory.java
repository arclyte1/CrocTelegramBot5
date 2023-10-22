package ru.croc.croctelegrambottemplate.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.croc.croctelegrambottemplate.constants.Constants;

import java.util.List;

/**
 * В это классе добавляются возможности выбора для пользователя.
 */
public class KeyboardFactory {

    /**
     * Данный список используется после ввода пользователем своего имени.
     * @return список возможных действий.
     */
    public static ReplyKeyboard getReplyToName() {
        KeyboardRow row = new KeyboardRow();
        row.add(Constants.INVEST_CALCULATION_NAME);
        // TODO: Добавить дополнительные параметры по необходимости.
        return new ReplyKeyboardMarkup(List.of(row));
    }

    // TODO: Дополнить выбор по необходимости.
}
