package Buttons;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface BotCommands {

     List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info"),
            new BotCommand("/theme", "chose bot theme"),
            new BotCommand("/auto","set auto mode")
    );

    String HELP_TEXT = "Привет,это бот с анекдотами. Я помогу выбрать тебе подходящий тебе по теме анекдот.Чтобы начать работу с ботом нажмите /start" +
            ". Для выбора темы используйте команду /theme,после чего отправьте первый символ темы,т.е букву или цифру. После выберите нужную тему. Для того,чтобы вернуться используйте /start или /theme  \n Команды:" +
            "\n/start - начать работу с ботом" +
            "\n/help  - помощь" +
            "\n/theme - выбрать тему анекдота";
    String THEME_TEXT = "Выберите первый символ темы[А-Я] [1-100]";

}