package service;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info"),
            new BotCommand("/theme", "chose bot theme"),
            new BotCommand("/setAuto","set auto-anekot sender")
    );

    String HELP_TEXT = "Привет,это бот с анекдотами. Я помогу выбрать тебе подходящий тебе по теме анекдот.Команды:" +
            "\n/start - начать работу с ботом" +
            "\n/help  - помощь" +
            "\n/theme - выбрать тему анекдота";

}