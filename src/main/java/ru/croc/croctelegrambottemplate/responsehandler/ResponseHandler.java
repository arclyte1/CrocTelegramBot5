package ru.croc.croctelegrambottemplate.responsehandler;

import static java.util.Objects.nonNull;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.croc.croctelegrambottemplate.constants.Constants;
import ru.croc.croctelegrambottemplate.states.UserState;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик ответов.
 * С помощью данного класса определяются сообщения, отправляемые пользователю.
 */
public abstract class ResponseHandler {

    /**
     * Мапа "Идентификатор чата пользователя" - "Имя пользователя"
     * Хранится для того, чтобы к пользователю можно было обращаться по имени.
     */
    protected static final Map<Long, String> MAP_CHATID_USERNAME = new HashMap<>();

    /**
     * Отправитель. При его помощи отправляются сообщения пользователю в Телеграм.
     */
    protected final SilentSender sender;

    /**
     * Состояния пользователя.
     * В определенный момент времени, бот может ожидать от пользователя ввода различной информации.
     * Необходимо для определений действий бота, в зависимости от контекста.
     */
    protected final Map<Long, UserState> chatStates;

    public ResponseHandler(SilentSender sender, DBContext db) {
        this.sender = sender;
        this.chatStates = db.getMap(Constants.CHAT_STATES);
    }

    /**
     * Метод, перехватывающий сообщения от пользователя.
     * Каждый обработчик переопределяет поведение данного метода.
     * @param chatId идентификатор чата с пользователем.
     * @param message сообщение, получаемое от пользователя.
     */
    protected abstract void handleMessage(long chatId, Message message);

    /**
     * Основной метод, вызывающий обработку сообщений.
     * @param chatId - идентификатор чата с пользователем.
     * @param message - сообщение, получаемое от пользователя.
     */
    public void handle(long chatId, Message message) {
        if (nonNull(message) && message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
            return;
        }
        handleMessage(chatId, message);
    }

    /**
     * Метод, необходимый для ввода значений пользователю.
     * @param chatId идентификатор чата пользователя.
     * @param text текст, который необходимо отобразить пользователю.
     * @param replyKeyboard список возможных ответных действий для пользователя.
     * @param awaitingReorder обновление состояния пользователя.
     */
    protected void promptWithKeyboardForState(
        long chatId,
        String text,
        ReplyKeyboard replyKeyboard,
        UserState awaitingReorder
    ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyKeyboard);
        sender.execute(sendMessage);
        chatStates.put(chatId, awaitingReorder);
    }

    /**
     * Обработчик "неправильных ответов" от пользователя.
     * @param chatId - идентификатор чата с пользователем.
     * @param replyKeyboard - возможный выбор ответов пользователя.
     */
    protected void unexpectedMessage(long chatId, ReplyKeyboard replyKeyboard) {
        String messageToReplay = MessageFormat.format(
            Constants.UNEXPECTED_MESSAGE,
            MAP_CHATID_USERNAME.getOrDefault(chatId, "username")
        );
        promptWithKeyboardForState(
            chatId,
            messageToReplay,
            replyKeyboard,
            getUserState()
        );
    }

    /**
     * Метод, завершающий чат с пользователем.
     * @param chatId идентификатор чата с пользователем.
     */
    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Спасибо за использование бота!\nНажми /start для повторного использования");
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
        MAP_CHATID_USERNAME.remove(chatId);
    }

    /**
     * Определяет активен ли пользователь.
     * @param chatId идентификатор чата с пользователем.
     * @return true, если пользователь активен, иначе false.
     */
    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

    /**
     * Вернуть состояние пользователя, который обрабатывает данный ответчик.
     * @return состояние пользователя.
     */
    public abstract UserState getUserState();
}
