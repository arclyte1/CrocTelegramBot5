# Шаблон для telegram-бота на Java Spring Boot 3.1

Официальная библиотека для телеграм ботов на Java - [https://telegraf.js.org/](https://github.com/rubenlagus/TelegramBots)

## Начальные условия работы с приложением

1. Необходимо установить Java 21 версии
2. Необходимо установить последнюю версию сборщика maven (>= 3.8.1)
3. Для комфортной разработки рекомендуется использовать IntelliJ IDEA

## Получение токена:
1. Перейти в официального бота BotFather - https://telegram.me/BotFather
2. Выбрать /start, а затем /newbot
3. Задать имя **CrocTestDrive2023<НомерКоманды>** и адрес **CrocTestDrive2023<НомерКоманды>_bot**

## Запуск приложения в IntelliJ IDEA:
1. Клонировать репозиторий командой git clone <адрес репозитория>
```
git clone https://gitlab.com/bossoness/croctelegrambottemplate.git
```
2. Вставить значение bot.token полученный от [BotFather](https://telegram.me/BotFather) в application.yml
3. Вставить значение bot.name, который вы указали в боте от [BotFather](https://telegram.me/BotFather) в application.yml
4. Собрать приложение при помощи команды:
```
mvn clean package
```
5. Запустить приложение через команду
```
mvn spring-boot:start
```
6. Запуск так же можно настроить через конфигурации IntelliJ IDEA Community Edition. [RunFromCommunityEdition](https://guneetgstar.medium.com/how-to-run-spring-boot-applications-on-intellij-idea-for-free-381a2661d409)

